package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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

@SpringBootTest
@Transactional
@Sql("classpath:delete.sql")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private MenuGroupDao menuGroupDao;

    private OrderTable orderTable;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("menuGroup"));

        Menu menu = menuDao.save(createMenu("menu", menuGroup.getId(), BigDecimal.valueOf(1000)));
        Menu menu2 = menuDao.save(createMenu("menu", menuGroup.getId(), BigDecimal.valueOf(1000)));

        orderLineItem1 = createOrderLineItem(null, menu.getId());
        orderLineItem2 = createOrderLineItem(null, menu2.getId());
        orderLineItem1.setQuantity(1);
        orderLineItem2.setQuantity(2);

        orderTable = orderTableDao.save(createOrderTable(false));
    }

    @Test
    void create() {
        Order order = createOrder(OrderStatus.MEAL, orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        Order savedOrder = orderService.create(order);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(2),
            () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId())
        );
    }

    @Test
    @DisplayName("주문 항목(Order Line item)은 1개 미만인 경우 에러")
    void createFailWhenOrderLineItemIsEmpty() {
        Order order = createOrder(OrderStatus.COOKING, orderTable.getId());
        order.setOrderLineItems(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create order Error: 주문 항목은 1개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문하려는 테이블은 empty 상태가 아니어야 한다.")
    void createFailWhenTableIsEmpty() {
        orderTable.setEmpty(true);
        orderTableDao.save(orderTable);

        Order order = createOrder(OrderStatus.COMPLETION, orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create order Error: empty여서 주문이 불가능합니다.");
    }

    @Test
    @DisplayName("주문의 목록을 불러올 수 있어야 한다.")
    void list() {
        Order order1 = createOrder(OrderStatus.COOKING, orderTable.getId());
        Order order2 = createOrder(OrderStatus.COOKING, orderTable.getId());

        order1.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        order2.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        orderService.create(order1);
        orderService.create(order2);

        List<Order> expectedOrders = orderService.list();

        assertThat(expectedOrders.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주문상태 변경이 가능하다.")
    void changeOrderStatus() {
        Order order = createOrder(OrderStatus.MEAL, orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        Order savedOrder = orderService.create(order);

        String orderStatus = savedOrder.getOrderStatus();

        Order orderToChange = createOrder(OrderStatus.COMPLETION, null);
        Order expectedOrder = orderService.changeOrderStatus(savedOrder.getId(), orderToChange);

        assertThat(expectedOrder.getOrderStatus()).isNotEqualTo(orderStatus);
        assertThat(expectedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문 상태가 결제 완료인 경우, 주문 상태를 변경할 수 없다.")
    void changeOrderStatusFail() {
        Order order = createOrder(OrderStatus.COOKING, orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(savedOrder.getId(), createOrder(OrderStatus.COMPLETION, null));

        Order orderToChange = createOrder(OrderStatus.COMPLETION, null);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), orderToChange))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeOrderStatus Error: 결제 완료된 주문은 상태를 변경할 수 없습니다.");
    }
}
