package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderService orderService;
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;
    private Order order;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("menu1");
        menu1.setPrice(BigDecimal.valueOf(1000));

        orderLineItem1 = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();
        orderLineItem1.setQuantity(1);
        orderLineItem2.setQuantity(2);
        orderLineItem1.setMenuId(1L);
        orderLineItem2.setMenuId(1L);

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);

        order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
    }

    @Test
    void create() {
        given(menuDao.countByIdIn(Arrays.asList(1L, 1L))).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable));
        given(orderDao.save(order)).willReturn(order);

        Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getOrderedTime()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderLineItems()).isEqualTo(order.getOrderLineItems());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    @Test
    @DisplayName("주문 항목(Order Line item)은 1개 이상이어야 한다.")
    void createFailWhenOrderLineItemIsEmpty() {
        order.setOrderLineItems(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create order Error: 주문 항목은 1개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문하려는 테이블은 empty 상태가 아니어야 한다.")
    void createFailWhenTableIsEmpty() {
        orderTable.setEmpty(true);
        given(menuDao.countByIdIn(Arrays.asList(1L, 1L))).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create order Error: empty여서 주문이 불가능합니다.");
    }

    @Test
    @DisplayName("주문의 목록을 불러올 수 있어야 한다.")
    void list() {
        Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderTableId(1L);
        order2.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        List<Order> orders = Arrays.asList(order, order2);

        given(orderDao.findAll()).willReturn(orders);
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        List<Order> foundOrders = orderService.list();

        assertThat(foundOrders.size()).isEqualTo(orders.size());
        assertThat(foundOrders.get(0)).isEqualTo(orders.get(0));
        assertThat(foundOrders.get(1)).isEqualTo(orders.get(1));

    }

    @Test
    void changeOrderStatus() {
        order.setOrderStatus(OrderStatus.MEAL.name());
        Order orderToChange = new Order();
        orderToChange.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(1L)).willReturn(java.util.Optional.ofNullable(order));
        given(orderDao.save(any())).willReturn(any());
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(anyList());

        String orderStatus = order.getOrderStatus();
        Order changedOrder = orderService.changeOrderStatus(1L, orderToChange);

        assertThat(orderStatus).isNotEqualTo(changedOrder.getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태가 결제 완료인 경우, 주문 상태를 변경할 수 없다.")
    void changeOrderStatusFail() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        Order orderToChange = new Order();
        orderToChange.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(1L)).willReturn(java.util.Optional.ofNullable(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderToChange))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeOrderStatus Error: 결제 완료된 주문은 상태를 변경할 수 없습니다.");
    }
}
