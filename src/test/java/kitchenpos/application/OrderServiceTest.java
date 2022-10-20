package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.SpringServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    @Nested
    class create_메소드는 {

        @Nested
        class 주문항목이_비어있는_경우 extends SpringServiceTest {

            private final Order order = new Order(1L, null, LocalDateTime.now(), new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문항목이 비어있습니다.");
            }
        }

        @Nested
        class 주문항목이_실제_메뉴와_불일치하는_경우 extends SpringServiceTest {

            private final Order order = new Order(1L, null, LocalDateTime.now(),
                    createOrderLineItem(new OrderLineItem(0L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("실제 메뉴로만 주문이 가능합니다.");
            }
        }

        @Nested
        class 없는_주문테이블을_입력한_경우 extends SpringServiceTest {

            private final Order order = new Order(null, null, LocalDateTime.now(),
                    createOrderLineItem(new OrderLineItem(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문테이블이_비어있는_경우 extends SpringServiceTest {

            private final Order order = new Order(1L, null, LocalDateTime.now(),
                    createOrderLineItem(new OrderLineItem(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문테이블이 비어있습니다.");
            }
        }

        @Nested
        class 정상적으로_주문요청한_경우 extends SpringServiceTest {

            private final Order order = new Order(1L, null, LocalDateTime.now(),
                    createOrderLineItem(new OrderLineItem(1L, 1)));

            @BeforeEach
            void setUp() {
                orderTableDao.save(new OrderTable(1L, null, 1, false));
            }

            @Test
            void 주문을_추가하고_반환한다() {
                Order actual = orderService.create(order);
                List<OrderLineItem> actualItems = orderLineItemDao.findAllByOrderId(actual.getId());

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actualItems).hasSize(1)
                );
            }
        }
    }

    @Nested
    class list_메소드는 {

        @Nested
        class 요청이_들어오는_경우 extends SpringServiceTest {

            @Test
            void 주문목록을_반환한다() {
                List<Order> actual = orderService.list();

                assertThat(actual).hasSize(0);
            }
        }
    }

    @Nested
    class changeOrderStatus_메소드는 {

        @Nested
        class 존재하지않는_주문_id를_입력받는_경우 extends SpringServiceTest {

            private final Order order = new Order(1L, null, LocalDateTime.now(),
                    createOrderLineItem(new OrderLineItem(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(0L, order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 이미_완료상태의_주문을_입력받는_경우 extends SpringServiceTest {

            private Long orderId;
            private final Order changeOrder = new Order(null, MEAL.name(), LocalDateTime.now(), new ArrayList<>());

            @BeforeEach
            void setUp() {
                orderId = orderDao.save(new Order(1L, COMPLETION.name(), LocalDateTime.now(), new ArrayList<>()))
                        .getId();
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, changeOrder))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_주문변경_요청할_경우 extends SpringServiceTest {

            private Long orderId;
            private final Order changeOrder = new Order(null, MEAL.name(), LocalDateTime.now(), new ArrayList<>());

            @BeforeEach
            void setUp() {
                orderId = orderDao.save(new Order(1L, COOKING.name(), LocalDateTime.now(), new ArrayList<>()))
                        .getId();
            }

            @Test
            void 주문변경_후_반환한다() {
                Order actual = orderService.changeOrderStatus(orderId, changeOrder);
                assertThat(actual.getOrderStatus()).isEqualTo(MEAL.name());
            }
        }
    }

    private List<OrderLineItem> createOrderLineItem(final OrderLineItem... orderLineItems) {
        return Arrays.stream(orderLineItems)
                .collect(Collectors.toList());
    }
}
