package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@ServiceTest
class OrderServiceTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();

        product1.setPrice(BigDecimal.valueOf(10_000L));
        product1.setName("후라이드 치킨");

        product2.setPrice(BigDecimal.valueOf(20_000L));
        product2.setName("양념 치킨");

        product3.setPrice(BigDecimal.valueOf(5_000L));
        product3.setName("시원한 아이스 아메리카노");

        Product savedProduct1 = productDao.save(product1);
        Product savedProduct2 = productDao.save(product2);
        Product savedProduct3 = productDao.save(product3);

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        MenuProduct menuProduct3 = new MenuProduct();

        menuProduct1.setProductId(savedProduct1.getId());
        menuProduct1.setQuantity(2L);

        menuProduct2.setProductId(savedProduct2.getId());
        menuProduct2.setQuantity(1L);

        menuProduct3.setProductId(savedProduct3.getId());
        menuProduct3.setQuantity(1L);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test_group");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();

        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(45_000L));
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2, menuProduct3));

        Menu savedMenu = menuDao.save(menu);

        OrderLineItem orderLineItem = new OrderLineItem();

        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2L);

        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);

        OrderTable table = new OrderTable();

        table.setEmpty(false);
        table.setNumberOfGuests(4);

        OrderTable savedTable = orderTableDao.save(table);

        Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(orderLineItems);

        Order actual = orderService.create(order);

        assertAll(
            () -> assertThat(actual).extracting(Order::getId).isNotNull(),
            () -> assertThat(actual.getOrderLineItems()).extracting(OrderLineItem::getOrderId)
                .containsOnly(actual.getId()),
            () -> assertThat(actual).extracting(Order::getOrderStatus).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(actual).extracting(Order::getOrderedTime).isNotNull()
        );
    }

    @DisplayName("주문을 추가할 시 주문 상품이 null이면 예외 처리한다.")
    @Test
    void createWithNullOrderProducts() {
        Order order = new Order();
        order.setOrderLineItems(null);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 주문 상품이 비어있으면 예외 처리한다.")
    @Test
    void createWithEmptyOrderProducts() {
        Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 존재하지 않는 MenuId일 경우 예외 처리한다.")
    @Test
    void createWithNotExistingMenu() {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(2L);

        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 존재하지 않는 테이블일 경우 예외 처리한다.")
    @Test
    void createWithNotExistingTable() {
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 테이블이 비어있다면 예외 처리한다.")
    @Test
    void createWithEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(4);

        OrderTable savedTable = orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 목록을 조회한다.")
    @Test
    void findList() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();

        product1.setPrice(BigDecimal.valueOf(10_000L));
        product1.setName("후라이드 치킨");

        product2.setPrice(BigDecimal.valueOf(20_000L));
        product2.setName("양념 치킨");

        product3.setPrice(BigDecimal.valueOf(5_000L));
        product3.setName("시원한 아이스 아메리카노");

        Product savedProduct1 = productDao.save(product1);
        Product savedProduct2 = productDao.save(product2);
        Product savedProduct3 = productDao.save(product3);

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        MenuProduct menuProduct3 = new MenuProduct();

        menuProduct1.setProductId(savedProduct1.getId());
        menuProduct1.setQuantity(2L);

        menuProduct2.setProductId(savedProduct2.getId());
        menuProduct2.setQuantity(1L);

        menuProduct3.setProductId(savedProduct3.getId());
        menuProduct3.setQuantity(1L);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test_group");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();

        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(45_000L));
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2, menuProduct3));

        Menu savedMenu = menuDao.save(menu);

        OrderLineItem orderLineItem = new OrderLineItem();

        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2L);

        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);

        OrderTable table = new OrderTable();

        table.setEmpty(false);
        table.setNumberOfGuests(4);

        OrderTable savedTable = orderTableDao.save(table);

        Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        Order savedOrder = orderDao.save(order);

        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItemDao.save(orderLineItem);

        List<Order> actual = orderService.list();

        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual).allSatisfy(it ->
                assertThat(it).extracting(Order::getOrderLineItems, InstanceOfAssertFactories.list(OrderLineItem.class))
                    .extracting(OrderLineItem::getOrderId)
                    .containsOnly(it.getId()))
        );
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus() {
        OrderTable table = new OrderTable();

        table.setEmpty(false);
        table.setNumberOfGuests(4);

        OrderTable savedTable = orderTableDao.save(table);

        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(savedTable.getId());

        Order savedOrder = orderDao.save(order);

        Order updateInfo = new Order();
        updateInfo.setOrderStatus(OrderStatus.MEAL.name());

        Order actual = orderService.changeOrderStatus(savedOrder.getId(), updateInfo);

        assertThat(actual.getOrderStatus()).isEqualTo(updateInfo.getOrderStatus());
    }

    @DisplayName("주문 상태를 수정할 시 해당되는 orderId가 없으면 예외 처리한다.")
    @Test
    void changeOrderStatusWithNotExistingOrderId() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 수정할 시 현재 주문이 Completion 상태이면 예외 처리한다.")
    @Test
    void changeOrderStatusWithCompletionStatus() {
        OrderTable table = new OrderTable();

        table.setEmpty(false);
        table.setNumberOfGuests(4);

        OrderTable savedTable = orderTableDao.save(table);

        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderTableId(savedTable.getId());

        Order savedOrder = orderDao.save(order);

        Order updateInfo = new Order();
        updateInfo.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), updateInfo))
            .isInstanceOf(IllegalArgumentException.class);
    }
}