package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderLineFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

class OrderServiceTest extends AbstractServiceTest {
    private OrderService orderService;
    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private MenuGroupDao menuGroupDao;
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
        orderDao = new JdbcTemplateOrderDao(dataSource);
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);

        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("OrderLineItem이 없는 Order의 경우 예외를 반환한다.")
    @Test
    void emptyOrderLineItem() {
        Order order = OrderFixture.createEmptyOrderLines();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderLineItems의 크기와 Menu들의 ID로 조회한 메뉴의 갯수가 다르면 예외를 반환한다.")
    @Test
    void orderLineDifferentMenuCounts() {
        Menu savedMenu = createMenu();
        OrderTable orderTable = OrderTableFixture.createBeforeSave();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Order order = OrderFixture.createWithStatus(savedOrderTable.getId(), OrderStatus.COMPLETION);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineFixture.createWithOrderAndMenu(savedMenu, 3),
            OrderLineFixture.createWithOrderAndMenu(savedMenu, 3)
        );
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장하려는 OrderTable이 존재하지 않으면 예외를 반환한다.")
    @Test
    void notExistOrderTable() {
        Menu savedMenu = createMenu();
        OrderTable notSavedOrderTable = OrderTableFixture.createBeforeSave();
        Order order = OrderFixture.createWithStatus(notSavedOrderTable.getId(), OrderStatus.COMPLETION);
        List<OrderLineItem> orderLineItems = Arrays.asList(OrderLineFixture.createWithOrderAndMenu(savedMenu, 3));
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장하려는 OrderTable이 비어 있으면 예외를 반환한다.")
    @Test
    void emptyOrderTable() {
        Menu savedMenu = createMenu();
        OrderTable orderTable = OrderTableFixture.createBeforeSave();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Order order = OrderFixture.createWithStatus(savedOrderTable.getId(), OrderStatus.COMPLETION);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineFixture.createWithOrderAndMenu(savedMenu, 3),
            OrderLineFixture.createWithOrderAndMenu(savedMenu, 3)
        );
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 Order가 저장되고, OrderLineItems도 수정된다.")
    @Test
    void create() {
        Menu savedMenu = createMenu();
        OrderTable orderTable = OrderTableFixture.createBeforeSave();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Order order = OrderFixture.createWithStatus(savedOrderTable.getId(), OrderStatus.COMPLETION);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineFixture.createWithOrderAndMenu(savedMenu, 3)
        );
        order.setOrderLineItems(orderLineItems);

        Order savedOrder = orderService.create(order);

        assertAll(
            () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(savedOrderTable.getId()),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
            () -> assertThat(savedOrder.getOrderLineItems())
                .extracting(OrderLineItem::getOrderId)
                .isEqualTo(Arrays.asList(savedOrder.getId()))
        );
    }

    @DisplayName("저장된 Order를 불러온다.")
    @Test
    void list() {
        Order order = createValidOrder();
        Order savedOrder = orderService.create(order);
        Order savedOrder2 = orderService.create(order);
        List<Order> orders = orderService.list();

        assertThat(orders)
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(savedOrder, savedOrder2));
    }

    @DisplayName("해당 Order가 존재하지 않는 경우 예외를 반환한다.")
    @Test
    void notFound() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(null, new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 식사가 완료된 곳은 상태를 변경할 수 없다.")
    @Test
    void completion() {
        Order savedOrder = orderService.create(createValidOrder());
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        Order updatedOrder = orderDao.save(savedOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(updatedOrder.getId(), updatedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 식사가 완료된 곳은 상태를 변경할 수 없다.")
    @Test
    void changeStatus() {
        Order savedOrder = orderService.create(createValidOrder());
        Order mealOrder = OrderFixture.createWithStatus(null, OrderStatus.MEAL);
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), mealOrder);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    private Order createValidOrder() {
        Menu savedMenu = createMenu();
        OrderTable orderTable = OrderTableFixture.createBeforeSave();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Order order = OrderFixture.createWithStatus(savedOrderTable.getId(), OrderStatus.COMPLETION);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineFixture.createWithOrderAndMenu(savedMenu, 3)
        );
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    private Menu createMenu() {
        List<Product> products = ProductFixture.getProducts(2000, 3000, 5000);
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.createWithoutId());
        Menu menu = MenuFixture.createWithMenuPriceAndProducts(10000, products, savedMenuGroup.getId());
        return menuDao.save(menu);
    }
}