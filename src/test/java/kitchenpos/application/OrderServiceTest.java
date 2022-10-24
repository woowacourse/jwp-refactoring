package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

class OrderServiceTest extends ServiceTest {

    @Autowired
    protected OrderService orderService;
    @Autowired
    protected MenuDao menuDao;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected OrderLineItemDao orderLineItemDao;
    @Autowired
    protected OrderTableDao orderTableDao;
    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(createdMenu.getId());

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable createdOrderTable = orderTableDao.save(orderTable);

        // when
        Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(createdOrderTable.getId());

        Order createdOrder = orderService.create(order);

        // then
        assertAll(
            () -> assertThat(createdOrder.getOrderTableId()).isEqualTo(createdOrderTable.getId()),
            () -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(createdOrder.getOrderLineItems()).hasSize(order.getOrderLineItems().size())
        );
    }

    @Test
    @DisplayName("주문 항목이 비어있을 수 없다")
    void emptyOrderLineItems() {
        // given, when
        Order order = new Order();

        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목에 해당하는 메뉴가 모두 저장되어 있어야 한다")
    void nonRegisteredMenu() {
        // given
        Long fakeMenuId = 999L;
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(fakeMenuId);

        // when
        Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));

        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목에 해당하는 주문 테이블이 함께 등록되어야 한다")
    void withoutOrderTable() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(createdMenu.getId());

        // when
        Long fakeOrderTableId = 999L;
        Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(fakeOrderTableId);

        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 수 없다")
    void emptyOrderTable() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(createdMenu.getId());

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable createdOrderTable = orderTableDao.save(orderTable);

        // when
        Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(createdOrderTable.getId());

        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void list() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);
        Order createdOrder = createOrder(createdMenu);

        // when
        List<Order> orders = orderService.list();

        // then
        assertAll(
            () -> assertThat(orders).hasSize(1),
            () -> assertThat(orders.get(0).getId()).isEqualTo(createdOrder.getId()),
            () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(createdOrder.getOrderTableId()),
            () -> assertThat(orders.get(0).getOrderLineItems()).hasSize(createdOrder.getOrderLineItems().size())
        );
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);
        Order createdOrder = createOrder(createdMenu);

        Order order = createOrder(createdMenu);
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when
        Order changedOrder = orderService.changeOrderStatus(createdOrder.getId(), order);

        // then
        assertAll(
            () -> assertThat(changedOrder.getId()).isEqualTo(createdOrder.getId()),
            () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus())
        );
    }

    @Test
    @DisplayName("등록된 주문 아이디로 변경할 수 없다")
    void nonRegisteredOrderId() {
        // given
        Order order = new Order();
        Long fakeOrderId = 999L;

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(fakeOrderId, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 완료된 주문은 변경할 수 없다")
    void alreadyCompletedOrder() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);
        Order createdOrder = createOrder(createdMenu);

        // when
        Order order = createOrder(createdMenu);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        Order completedOrder = orderService.changeOrderStatus(createdOrder.getId(), order);

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private MenuGroup createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("testGroup");
        return menuGroupDao.save(menuGroup);
    }

    private Menu createMenu(MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.setName("testMenu");
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(menuGroup.getId());
        return menuDao.save(menu);
    }

    private Order createOrder(Menu menu) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable createdOrderTable = orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(createdOrderTable.getId());

        return orderService.create(order);
    }
}
