package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderLineItemRequest;
import kitchenpos.application.request.OrderTableUpdateRequest;
import kitchenpos.application.request.OrderUpdateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class OrderLineItem이_없는_경우 extends ServiceTest {

            private final OrderCreateRequest request = new OrderCreateRequest(1L, null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("OrderLineItem이 존재하지 않습니다.");
            }
        }

        @Nested
        class 입력받은_Menu가_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_MENU_ID = -1L;

            private final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemRequest(NOT_EXIST_MENU_ID, 1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 메뉴가 존재합니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable이_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_ORDER_TABLE_ID = -1L;

            private final OrderCreateRequest request = new OrderCreateRequest(NOT_EXIST_ORDER_TABLE_ID,
                    List.of(new OrderLineItemRequest(1L, 1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 OrderTable 입니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable의_상태가_empty인_경우 extends ServiceTest {

            private final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemRequest(1L, 1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 OrderTable이 empty 상태 입니다.");
            }
        }

        @Nested
        class 정상적인_입력을_받을_경우 extends ServiceTest {

            @Test
            void Order를_생성하고_반환한다() {
                final OrderTable orderTable = tableService.changeEmpty(1L, new OrderTableUpdateRequest(5, false));
                final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                        List.of(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L)));
                final Order actual = orderService.create(request);

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getOrderTableId()).isEqualTo(1L),
                        () -> assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now()),
                        () -> assertThat(actual.getOrderStatus()).isEqualTo(COOKING.name()),
                        () -> assertThat(actual.getOrderLineItems())
                                .extracting("menuId", "quantity")
                                .containsExactly(tuple(1L, 1L), tuple(2L, 1L))
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출하는_경우 extends ServiceTest {

            private static final int EXPECT_SIZE = 1;

            @Test
            void Order의_목록을_반환한다() {
                final OrderTable orderTable = tableService.changeEmpty(1L, new OrderTableUpdateRequest(5, false));
                final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                        List.of(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L)));

                orderService.create(request);

                final List<Order> actual = orderService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }

    @Nested
    class changeOrderStatus_메서드는 {

        @Nested
        class 입력받은_Order가_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_ORDER_ID = -1;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_ORDER_ID, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 Order 입니다.");
            }
        }

        @Nested
        class 입력받은_Order의_상태가_COMPLETION인_경우 extends ServiceTest {

            @Test
            void 예외가_발생한다() {
                final Order actual = orderDao.save(new Order(1L, COMPLETION.name()));

                assertThatThrownBy(() -> orderService.changeOrderStatus(actual.getId(), null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 완료된 Order의 상태는 변경할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_입력일_경우 extends ServiceTest {

            private final OrderUpdateRequest request = new OrderUpdateRequest(COMPLETION.name());

            @Test
            void Order의_상태를_변경한다() {
                final OrderTable orderTable = tableService.changeEmpty(1L, new OrderTableUpdateRequest(5, false));
                final Order order = orderService.create(new OrderCreateRequest(orderTable.getId(),
                        List.of(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L))));

                final Order actual = orderService.changeOrderStatus(order.getId(), request);

                assertThat(actual.getOrderStatus()).isEqualTo(COMPLETION.name());
            }
        }
    }
}
