package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singletonList;
import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.support.IntegrationServiceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationServiceTest {

    private static final OrderLineItemRequest ORDER_LINE_ITEM = new OrderLineItemRequest(1L, 1L);
    private static final List<OrderLineItemRequest> ORDER_LINE_ITEMS = singletonList(ORDER_LINE_ITEM);

    @Test
    void create_메서드는() {

        @Nested
        class 주문항목이_비어있는_경우 {

            private final List<OrderLineItemRequest> EMPTY_ORDER_LINE_ITEMS = Lists.emptyList();

            private final OrderRequest orderRequest = new OrderRequest(1L, null, now(), EMPTY_ORDER_LINE_ITEMS);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 항목이 비어있을 수 없습니다.");
            }
        }

        @Nested
        class 존재하지_않는_메뉴를_주문할_경우 {

            private static final long 존재하지_않는_MENU_ID = -1L;
            private final OrderRequest orderRequest =
                    new OrderRequest(1L, null, now(), List.of(new OrderLineItemRequest(존재하지_않는_MENU_ID, 1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하는 메뉴에 대해서만 주문이 가능합니다.");
            }
        }

        @Nested
        class 존재하지_않는_주문테이블을_입력한_경우 {

            private final long 존재하지_않는_주문테이블_ID = -1L;
            private final OrderRequest orderRequest =
                    new OrderRequest(존재하지_않는_주문테이블_ID, null, now(), ORDER_LINE_ITEMS);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하는 주문 테이블에 대해서만 주문이 가능합니다.");
            }
        }

        @Nested
        class 주문테이블이_비어있는_경우 {

            private final OrderRequest orderRequest = new OrderRequest(1L, null, now(), ORDER_LINE_ITEMS);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문테이블이 비어있습니다.");
            }
        }

        @Nested
        class 정상적으로_주문요청한_경우 extends IntegrationServiceTest {

            private final OrderRequest orderRequest = new OrderRequest(1L, null, now(), ORDER_LINE_ITEMS);
            private Long savedOrderTableId;

            @BeforeEach
            void setUp() {
                OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(1L, 1, false, null));
                savedOrderTableId = savedOrderTable.getId();
            }

            @Test
            void 주문을_추가하고_반환한다() {
                Order actual = orderService.create(orderRequest);
                List<OrderLineItem> actualItems = orderLineItemRepository.findAllByOrderId(actual.getId());

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actualItems).hasSize(1)
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Test
        void 주문목록을_반환한다() {
            List<Order> actual = orderService.list();

            assertThat(actual).hasSize(0);
        }
    }

    @Nested
    class changeOrderStatus_메서드는 {

        @Nested
        class 존재하지않는_주문의_상태를_변경할_경우 {

            private static final long NOT_EXISTS_ORDER_ID = -1L;

            private final OrderRequest orderRequest = new OrderRequest(1L, MEAL.name(), now(),
                    ORDER_LINE_ITEMS);

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXISTS_ORDER_ID, orderRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 주문입니다.");
            }
        }

        @Nested
        class 이미_계산완료된_주문의_상태를_변경하려는_경우 extends IntegrationServiceTest {

            private Long savedOrderId;

            @BeforeEach
            void setUp() {
                List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1L));
                savedOrderId = orderRepository.save(new Order(COMPLETION, now(), null, orderLineItems))
                        .getId();
            }

            @Test
            void 예외를_발생한다() {

                assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId,
                        new OrderRequest(null, MEAL.name(), now(), ORDER_LINE_ITEMS)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 계산 완료된 주문의 상태를 변경할 수 없습니다.");
            }
        }
    }
}
