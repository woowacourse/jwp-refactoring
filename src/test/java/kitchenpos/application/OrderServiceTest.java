package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Nested
    @DisplayName("주문 목록을 생성할 때 ")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menu =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menu);

            final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
            final OrderTable orderTable = new OrderTable(null, null, 1, false);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, List.of(orderLineItem));

            // when
            final Order savedOrder = orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(savedOrder.getId()).isEqualTo(1L),
                    () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("주문 항목이 빈 값이거나 컬렉션이 비어있는 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderLineItemsAreNull(List<OrderLineItem> orderLineItems) {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menu =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menu);

            final OrderTable orderTable = new OrderTable(null, null, 1, false);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴의 개수와 주문 항목의 개수가 다른 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderLineItemsAndMenuHasDifferentCount() {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menuA =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menuA);

            final OrderLineItem orderLineItemA = new OrderLineItem(null, null, 1L, 1);
            final OrderLineItem orderLineItemB = new OrderLineItem(null, null, 2L, 1);
            final OrderTable orderTable = new OrderTable(null, null, 1, false);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null,
                    List.of(orderLineItemA, orderLineItemB));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 ID가 존재하지 않는다면 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIdNonExist() {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menu =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menu);

            final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
            final OrderTable orderTable = new OrderTable(null, null, 1, false);
            orderTableDao.save(orderTable);

            final Order order = new Order(null, null, null, null, List.of(orderLineItem));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIsEmpty() {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menu =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menu);

            final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
            final OrderTable orderTable = new OrderTable(null, null, 1, true);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, List.of(orderLineItem));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("주문 목록은 정상적으로 조회된다.")
    void list() {
        // given
        final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
        final MenuGroup menuGroup = new MenuGroup(null, "치킨");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        final Menu menu =
                new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
        menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
        final OrderTable orderTable = new OrderTable(null, null, 1, false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order orderA = new Order(null, savedOrderTable.getId(), null, null, List.of(orderLineItem));
        final Order orderB = new Order(null, savedOrderTable.getId(), null, null, List.of(orderLineItem));

        orderService.create(orderA);
        orderService.create(orderB);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(2);
    }

    @Nested
    @DisplayName("주문의 상태를 변경할 때 ")
    class ChangeOrderStatus {

        @Test
        @DisplayName("정상적으로 변경된다.")
        void changeOrderStatus() {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menu =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menu);

            final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
            final OrderTable orderTable = new OrderTable(null, null, 1, false);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, List.of(orderLineItem));
            final Order savedOrder = orderService.create(order);

            final Order newOrder = new Order(null, savedOrderTable.getId(), OrderStatus.COOKING.name(), null,
                    List.of(orderLineItem));

            // when
            final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

            // then
            assertAll(
                    () -> assertThat(changedOrder.getId()).isEqualTo(1L),
                    () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
            );
        }

        @Test
        @DisplayName("주문 ID에 해당되는 주문이 존재하지 않는 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderNonExist() {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menu =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menu);

            final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
            final OrderTable orderTable = new OrderTable(null, null, 1, false);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, List.of(orderLineItem));
            orderService.create(order);

            final Order newOrder = new Order(null, savedOrderTable.getId(), OrderStatus.COOKING.name(), null,
                    List.of(orderLineItem));

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(2L, newOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("기존 주문의 상태가 결제완료라면 예외가 발생한다.")
        void throwsExceptionWhenOrderStatusIsCompletion() {
            // given
            final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1);
            final MenuGroup menuGroup = new MenuGroup(null, "치킨");
            final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            final Menu menu =
                    new Menu(null, "후라이드 치킨", new BigDecimal("15000.00"), savedMenuGroup.getId(), List.of(menuProduct));
            menuDao.save(menu);

            final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
            final OrderTable orderTable = new OrderTable(null, null, 1, false);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, List.of(orderLineItem));
            final Order savedOrder = orderService.create(order);
            savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
            final Order completedOrder = orderDao.save(savedOrder);

            final Order newOrder =
                    new Order(null, savedOrderTable.getId(), OrderStatus.COOKING.name(), null, List.of(orderLineItem));

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), newOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
