package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixtures.TestFixtures.메뉴_그룹_생성;
import static kitchenpos.fixtures.TestFixtures.메뉴_생성;
import static kitchenpos.fixtures.TestFixtures.주문_생성;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성;
import static kitchenpos.fixtures.TestFixtures.주문_항목_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_주문이_입력되면 extends ServiceTest {

            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 1L, 5);
            private final MenuGroup menuGroup = 메뉴_그룹_생성("한마리메뉴");
            private final Menu menu = 메뉴_생성("치킨", BigDecimal.valueOf(1_000L), 1L, new ArrayList<>());
            private final OrderTable orderTable = 주문_테이블_생성(null, 5, false);
            private final Order order = 주문_생성(1L, MEAL.name(), LocalDateTime.now(), List.of(orderLineItem));

            @BeforeEach
            void setUp() {
                menuGroupDao.save(menuGroup);
                menuDao.save(menu);
                orderTableDao.save(orderTable);
            }

            @Test
            void 해당_주문을_반환한다() {
                final Order actual = orderService.create(order);

                assertAll(
                        () -> assertThat(actual).isNotNull(),
                        () -> assertThat(actual.getOrderStatus()).isEqualTo(COOKING.name())
                );
            }
        }

        @Nested
        class 빈_주문_항목으로_주문이_입력되면 extends ServiceTest {

            private final Order order = 주문_생성(null, MEAL.name(), LocalDateTime.now(), null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 존재하지_않는_메뉴로_주문이_입력되면 extends ServiceTest {

            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 1L, 5);
            private final Order order = 주문_생성(null, MEAL.name(), LocalDateTime.now(), List.of(orderLineItem));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블없이_주문을_입력하면 extends ServiceTest {

            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 1L, 5);
            private final MenuGroup menuGroup = 메뉴_그룹_생성("한마리메뉴");
            private final Menu menu = 메뉴_생성("치킨", BigDecimal.valueOf(1_000L), 1L, new ArrayList<>());
            private final Order order = 주문_생성(null, MEAL.name(), LocalDateTime.now(), List.of(orderLineItem));

            @BeforeEach
            void setUp() {
                menuGroupDao.save(menuGroup);
                menuDao.save(menu);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 비어있는_주문_테이블로_주문을_입력하면 extends ServiceTest {

            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 1L, 5);
            private final MenuGroup menuGroup = 메뉴_그룹_생성("한마리메뉴");
            private final Menu menu = 메뉴_생성("치킨", BigDecimal.valueOf(1_000L), 1L, new ArrayList<>());
            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);
            private final Order order = 주문_생성(1L, MEAL.name(), LocalDateTime.now(), List.of(orderLineItem));

            @BeforeEach
            void setUp() {
                menuGroupDao.save(menuGroup);
                menuDao.save(menu);
                orderTableDao.save(orderTable);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되면 extends ServiceTest {

            @Test
            void 모든_주문을_반환한다() {
                final List<Order> orders = orderService.list();
                assertThat(orders).isEmpty();
            }
        }
    }

    @Nested
    class changeOrderStatus_메서드는 {

        @Nested
        class 주문_id와_주문이_입력되면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);
            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 1L, 5);
            private final Order order = 주문_생성(1L, MEAL.name(), LocalDateTime.now(), List.of(orderLineItem));
            final Order orderToUpdateTo = 주문_생성(null, COMPLETION.name(), LocalDateTime.now(), null);
            private Order savedOrder;

            @BeforeEach
            void setUp() {
                orderTableDao.save(orderTable);
                savedOrder = orderDao.save(order);
            }

            @Test
            void 주문상태를_변경하고_주문을_반환한다() {
                final Order updatedOrder = orderService.changeOrderStatus(savedOrder.getId(), orderToUpdateTo);

                assertThat(updatedOrder.getOrderStatus()).isEqualTo(orderToUpdateTo.getOrderStatus());
            }
        }
    }
}
