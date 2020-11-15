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
        List<Product> products = ProductFixture.getProducts(2000, 3000, 5000);

        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.createWithoutId());
        Menu menu = MenuFixture.createWithMenuPriceAndProducts(10000, products, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        OrderTable orderTable = OrderTableFixture.createBeforeSave();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Order order = OrderFixture.createWithStatus(savedOrderTable.getId(), OrderStatus.COMPLETION);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineFixture.createWithOrderAndMenu(menu, 3),
            OrderLineFixture.createWithOrderAndMenu(menu, 3)
        );
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장하려는 OrderTable이 존재하지 않으면 예외를 반환한다.")
    @Test
    void notExistOrderTable() {
        List<Product> products = ProductFixture.getProducts(2000, 3000, 5000);
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.createWithoutId());
        Menu menu = MenuFixture.createWithMenuPriceAndProducts(10000, products, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);
        OrderTable orderTable = OrderTableFixture.createBeforeSave();
        Order order = OrderFixture.createWithStatus(orderTable.getId(), OrderStatus.COMPLETION);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineFixture.createWithOrderAndMenu(menu, 3),
            OrderLineFixture.createWithOrderAndMenu(menu, 3)
        );
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장하려는 OrderTable이 비어 있으면 예외를 반환한다.")
    @Test
    void emptyOrderTable() {
        List<Product> products = ProductFixture.getProducts(2000, 3000, 5000);
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.createWithoutId());
        Menu menu = MenuFixture.createWithMenuPriceAndProducts(10000, products, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);
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
        List<Product> products = ProductFixture.getProducts(2000, 3000, 5000);
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.createWithoutId());
        Menu menu = MenuFixture.createWithMenuPriceAndProducts(10000, products, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);
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
}