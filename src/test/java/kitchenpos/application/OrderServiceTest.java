package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @BeforeEach
    void setUp() {
        menuDao.save(new Menu("후라이드", new BigDecimal(1000), createMenuGroup(), createMenuProducts()));
    }

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_주문이_입력되면 {

            private final Order order = new Order(createOrderTable(), MEAL.name(), LocalDateTime.now(),
                    createOrderLineItems(new OrderLineItem(1L, 1L, 5)));

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
        class 빈_주문_항목으로_주문이_입력되면 {

            private final Order order = new Order(createOrderTable(), MEAL.name(), LocalDateTime.now(), null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 존재하지_않는_메뉴로_주문이_입력되면 {

            private final Order order = new Order(createOrderTable(), MEAL.name(), LocalDateTime.now(),
                    createOrderLineItems(new OrderLineItem(1L, 2L, 5)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블없이_주문을_입력하면 {

            private final Order order = new Order(null, MEAL.name(), LocalDateTime.now(),
                    createOrderLineItems(new OrderLineItem(1L, 1L, 5)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 비어있는_주문_테이블로_주문을_입력하면 {

            private final Order order = new Order(createEmptyOrderTable(), MEAL.name(), LocalDateTime.now(),
                    createOrderLineItems(new OrderLineItem(1L, 1L, 5)));

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
        class 호출되면 {

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
        class 주문_id와_주문이_입력되면 {

            private Order savedOrder;

            @BeforeEach
            void setUp() {
                final Order order = new Order(createOrderTable(), MEAL.name(), LocalDateTime.now(),
                        createOrderLineItems(new OrderLineItem(1L, 1L, 5)));
                savedOrder = orderDao.save(order);
            }

            @Test
            void 주문상태를_변경하고_주문을_반환한다() {
                final Order orderToUpdateTo = new Order(null, COOKING.name(), LocalDateTime.now(), null);
                final Order updatedOrder = orderService.changeOrderStatus(savedOrder.getId(), orderToUpdateTo);

                assertThat(updatedOrder.getOrderStatus()).isEqualTo(orderToUpdateTo.getOrderStatus());
            }
        }
    }

    private Long createOrderTable() {
        final OrderTable orderTable = new OrderTable(createTableGroup(), 5, false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return savedOrderTable.getId();
    }

    private Long createEmptyOrderTable() {
        final OrderTable orderTable = new OrderTable(createTableGroup(), 0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return savedOrderTable.getId();
    }

    private Long createTableGroup() {
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()));
        return savedTableGroup.getId();
    }

    private List<OrderLineItem> createOrderLineItems(final OrderLineItem... orderLineItems) {
        return Arrays.stream(orderLineItems)
                .collect(Collectors.toList());
    }

    private Long createMenuGroup() {
        final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("한마리치킨"));
        return savedMenuGroup.getId();
    }

    private List<MenuProduct> createMenuProducts() {
        final Product savedProduct = productDao.save(new Product("후라이드", new BigDecimal(1000)));
        final MenuProduct menuProduct = new MenuProduct(1L, savedProduct.getId(), 1);
        return List.of(menuProduct);
    }
}
