package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.support.IntegrationServiceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class OrderServiceTest extends IntegrationServiceTest {

    private static final OrderLineItem ORDER_LINE_ITEM = new OrderLineItem(1L, 1L);
    private static final List<OrderLineItem> ORDER_LINE_ITEMS = singletonList(ORDER_LINE_ITEM);

    @Test
    void create() {

        @Nested
        class 주문항목이_비어있는_경우 {

            private final List<OrderLineItem> EMPTY_ORDER_LINE_ITEMS = Lists.emptyList();

            private final Order order = new Order(1L, null, now(), EMPTY_ORDER_LINE_ITEMS);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 항목이 비어있을 수 없습니다.");
            }
        }

        @Nested
        class 존재하지_않는_메뉴를_주문할_경우 {

            private static final long 존재하지_않는_MENU_ID = -1L;
            private final Order order =
                    new Order(1L, null, now(), asList(new OrderLineItem(존재하지_않는_MENU_ID, 1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하는 메뉴에 대해서만 주문이 가능합니다.");
            }
        }

        @Nested
        class 없는_주문테이블을_입력한_경우 {

            private final long 존재하지_않는_주문테이블_ID = -1L;
            private final Order order =
                    new Order(존재하지_않는_주문테이블_ID, null, now(), ORDER_LINE_ITEMS);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하는 주문 테이블에 대해서만 주문이 가능합니다.");
            }
        }

        @Nested
        class 주문테이블이_비어있는_경우 {

        }

        @Nested
        class 정상적으로_주문요청한_경우 {


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

            private final Order order = new Order(1L, OrderStatus.MEAL.name(), now(), ORDER_LINE_ITEMS);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXISTS_ORDER_ID, order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 주문입니다.");
            }
        }


        @Nested
        class 이미_계산완료된_주문의_상태를_변경하려는_경우 {

            final Order savedOrder =
                    orderDao.save(new Order(1L, OrderStatus.COMPLETION.name(), now(), ORDER_LINE_ITEMS));
            final Long savedOrderId = savedOrder.getId();
            final Order orderToChange = new Order(null, OrderStatus.MEAL.name(), now(), ORDER_LINE_ITEMS);

            @Test
            void 예외를_발생한다() {

                assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, orderToChange))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 계산 완료된 주문의 상태를 변경할 수 없습니다.");
            }
        }
    }
}