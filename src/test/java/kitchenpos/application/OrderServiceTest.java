package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.TableServiceTest.createOrderTable;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
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

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Transactional
    @DisplayName("주문을 추가할 수 있다.")
    @Test
    void createOrder() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = createMenu("후라이드치킨", 16_000, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        OrderTable savedTable = orderTableDao.save(new OrderTable());

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1);
        Order order = createOrder(savedTable.getId(), orderLineItem);
        Order savedOrder = orderService.create(order);

        assertAll(() -> {
            assertThat(savedOrder).isNotNull();
            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
            assertThat(savedOrder.getOrderLineItems()).hasSize(1);
        });
    }

    @Transactional
    @DisplayName("예외: 빈 OrderLineItems 로 Order 추가")
    @Test
    void createOrderWithoutOrderLineItem() {
        OrderTable savedTable = orderTableDao.save(new OrderTable());

        Order order = createOrder(savedTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 존재하지 않는 Menu로 Order 추가")
    @Test
    void createOrderWithInvalidMenuId() {
        OrderTable savedTable = orderTableDao.save(new OrderTable());

        OrderLineItem orderLineItem = createOrderLineItem(100L, 1);
        Order order = createOrder(savedTable.getId(), orderLineItem);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 존재하지 않는 OrderTable로 Order 추가")
    @Test
    void createOrderWithInvalidOrderTableId() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = createMenu("후라이드치킨", 16_000, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1);
        Order order = createOrder(100L, orderLineItem);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 빈 OrderTable로 Order 추가")
    @Test
    void createOrderWithEmptyOrderTableId() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = createMenu("후라이드치킨", 16_000, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        OrderTable emptyTable = createOrderTable(true);
        OrderTable savedEmptyTable = orderTableDao.save(emptyTable);

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1);
        Order order = createOrder(savedEmptyTable.getId(), orderLineItem);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("전체 주문을 조회할 수 있다.")
    @Test
    void findAllOrdersWithOrderLineItems() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = createMenu("후라이드치킨", 16_000, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        OrderTable firstTable = orderTableDao.save(new OrderTable());
        OrderTable secondTable = orderTableDao.save(new OrderTable());

        Order firstOrder = createOrder(firstTable.getId());
        Order savedFirstOrder = orderDao.save(firstOrder);

        OrderLineItem firstItem = createOrderLineItem(savedMenu.getId(), 1, savedFirstOrder.getId());
        orderLineItemDao.save(firstItem);


        Order secondOrder = createOrder(secondTable.getId());
        Order savedSecondOrder = orderDao.save(secondOrder);

        OrderLineItem secondItem = createOrderLineItem(savedMenu.getId(), 3, savedSecondOrder.getId());
        orderLineItemDao.save(secondItem);

        List<Order> orders = orderService.list();

        assertAll(() -> {
            assertThat(orders).hasSize(2);
            assertThat(orders).extracting(Order::getOrderTableId).containsOnly(firstTable.getId(), secondTable.getId());
        });
    }

    @Transactional
    @DisplayName("Order의 상태를 수정할 수 있다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    @ParameterizedTest
    void changeOrderStatus(OrderStatus status) {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = createMenu("후라이드치킨", 16_000, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        OrderTable savedTable = orderTableDao.save(new OrderTable());

        Order order = createOrder(savedTable.getId());
        Order savedOrder = orderDao.save(order);

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1, savedOrder.getId());
        orderLineItemDao.save(orderLineItem);

        Order orderWithNewStatus = createOrder(status);

        Order updatedOrder = orderService.changeOrderStatus(savedOrder.getId(), orderWithNewStatus);

        assertAll(() -> {
            assertThat(updatedOrder).isNotNull();
            assertThat(updatedOrder.getOrderStatus()).isEqualTo(status.name());
            assertThat(updatedOrder.getOrderLineItems()).isNotEmpty()
                    .extracting(OrderLineItem::getMenuId).containsOnly(savedMenu.getId());
        });
    }

    @DisplayName("예외: 존재하지 않는 Order의 상태를 수정")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    @ParameterizedTest
    void changeOrderStatusWithInvalidOrder(OrderStatus status) {
        Order order = createOrder(status);

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: Completion 상태의 Order의 상태를 수정")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    @ParameterizedTest
    void changeOrderStatusWithCompletedOrder(OrderStatus status) {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = createMenu("후라이드치킨", 16_000, savedMenuGroup.getId());
        Menu savedMenu = menuDao.save(menu);

        OrderTable savedTable = orderTableDao.save(new OrderTable());

        Order order = createOrder(savedTable.getId());
        order.setOrderStatus(COMPLETION.name());
        Order savedOrder = orderDao.save(order);

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1, savedOrder.getId());
        orderLineItemDao.save(orderLineItem);

        Order orderWithNewStatus = createOrder(status);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), orderWithNewStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }


    private Order createOrder(Long tableId, OrderLineItem... items) {
        List<OrderLineItem> orderLineItems = Arrays.asList(items);
        Order order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    private Order createOrder(OrderStatus status) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        return order;
    }

    private OrderLineItem createOrderLineItem(Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private OrderLineItem createOrderLineItem(Long menuId, int quantity, Long orderId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setOrderId(orderId);
        return orderLineItem;
    }

    private Menu createMenu(String name, int price, Long groupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(groupId);
        return menu;
    }

    static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}