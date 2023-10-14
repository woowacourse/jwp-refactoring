package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;

class OrderServiceTest extends ServiceTest{

    @Autowired
    private OrderService orderService;

    private OrderLineItem orderLineItem;
    private Order order;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderLineItem = makeOrderLineItem();
        order = makeOrder(orderLineItem);
        orderTable = makeOrderTable();
    }

    @Test
    void 주문을_생성한다() {
        Mockito.when(menuDao.countByIdIn(anyList()))
                .thenReturn(1L);
        Mockito.when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(orderTable));
        Mockito.when(orderDao.save(any(Order.class)))
                .thenReturn(order);
        Mockito.when(orderLineItemDao.save(any(OrderLineItem.class)))
                .thenReturn(orderLineItem);

        Order saved = orderService.create(order);
        assertThat(saved.getId()).isEqualTo(order.getId());

    }

    @Test
    void 전체_주문을_조회한다() {
        Mockito.when(orderDao.findAll())
                .thenReturn(List.of(order, order));
        Mockito.when(orderLineItemDao.findAllByOrderId(anyLong()))
                .thenReturn(List.of(orderLineItem, orderLineItem));

        List<Order> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void 주문_상태를_변경한다() {
        Order newOrder = makeOrder(orderLineItem);
        newOrder.setOrderStatus(OrderStatus.MEAL.toString());

        Mockito.when(orderDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(order));

        Mockito.when(orderDao.save(any(Order.class)))
                .thenReturn(newOrder);
        Mockito.when(orderLineItemDao.findAllByOrderId(anyLong()))
                .thenReturn(List.of(orderLineItem, orderLineItem));

        Order updated = orderService.changeOrderStatus(1L, newOrder);
        assertThat(updated.getOrderStatus()).isEqualTo(newOrder.getOrderStatus());
    }

    private OrderTable makeOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }

    private Order makeOrder(OrderLineItem orderLineItem) {
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.toString());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(List.of(orderLineItem));
        return order;
    }

    private OrderLineItem makeOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);
        return orderLineItem;
    }
}
