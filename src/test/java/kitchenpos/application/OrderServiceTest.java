package kitchenpos.application;

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

        private List<OrderLineItem> createOrderLineItem(final OrderLineItem... orderLineItems) {
            return Arrays.stream(orderLineItems)
                    .collect(Collectors.toList());
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
}
