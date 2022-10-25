package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(1L, 1)));

            @Test
            void 주문을_추가한다() {
                Order actual = orderService.create(order);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
                    assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                    assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now());
                    assertThat(actual.getOrderLineItems()).hasSize(1);
                });
            }
        }

        @Nested
        class 주문_항목이_비어있을_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order order = new Order(orderTable.getId(), List.of());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 항목은 비어있을 수 없습니다.");
            }
        }

        @Nested
        class 주문_항목에_중복되는_메뉴가_있을_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order order = new Order(orderTable.getId(),
                    List.of(new OrderLineItem(1L, 1), new OrderLineItem(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 항목엔 중복되는 메뉴나 존재하지 않는 메뉴가 있을 수 없습니다.");
            }
        }

        @Nested
        class 주문_항목에_존재하지_않는_메뉴가_있을_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order order = new Order(orderTable.getId(),
                    List.of(new OrderLineItem(1L, 1), new OrderLineItem(0L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 항목엔 중복되는 메뉴나 존재하지 않는 메뉴가 있을 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않을_경우 {

            private final Order order = new Order(0L, List.of(new OrderLineItem(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 주문_테이블이_비활성화된_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, true));
            private final Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("비활성화된 주문 테이블은 주문을 받을 수 없습니다.");
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order order = orderService.create(
                    new Order(orderTable.getId(), List.of(new OrderLineItem(1L, 1))));

            @Test
            void 주문_목록을_반환한다() {
                List<Order> orders = orderService.list();

                assertThat(orders).isNotEmpty();
            }
        }
    }

    @Nested
    class changeOrderStatus_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order order = orderService.create(
                    new Order(orderTable.getId(), List.of(new OrderLineItem(1L, 1))));
            private final Order orderToBoChanged = new Order(OrderStatus.COOKING.name());

            @Test
            void 주문_상태를_변경한다() {
                Order actual = orderService.changeOrderStatus(order.getId(), orderToBoChanged);

                assertAll(() -> {
                    assertThat(actual.getId()).isEqualTo(order.getId());
                    assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                });
            }
        }

        @Nested
        class 주문이_존재하지_않을_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order orderToBoChanged = new Order(OrderStatus.COOKING.name());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(0L, orderToBoChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문이 존재하지 않습니다.");
            }
        }

        @Nested
        class 주문이_이미_계산_완료_상태인_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            private final Order order = orderDao.save(
                    new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                            List.of(new OrderLineItem(1L, 1))));
            private final Order orderToBoChanged = new Order(OrderStatus.COOKING.name());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderToBoChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문이 이미 계산 완료되었습니다.");
            }
        }
    }
}
