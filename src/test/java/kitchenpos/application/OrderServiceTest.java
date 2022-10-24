package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    @Test
    void create() {
        // given
        OrderTable orderTable = getOrderTable(1L, 1L, 3, false);

        Order inputOrder = getOrder(null, orderTable.getId(), null, null, new ArrayList<>());
        Order order = getOrder(1L, orderTable.getId(), OrderStatus.COOKING.name(), null, new ArrayList<>());

        OrderLineItem orderLineItem = getOrderLineItem(1L, order.getId(), 1L, 3);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        inputOrder.setOrderLineItems(orderLineItems);
        order.setOrderLineItems(orderLineItems);

        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
        given(menuDao.countByIdIn(anyList()))
                .willReturn((long) orderLineItems.size());
        given(orderDao.save(any()))
                .willReturn(order);

        // when
        Order actual = orderService.create(inputOrder);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void list() {
        // given
        OrderTable orderTable = getOrderTable(1L, 1L, 3, false);
        Order order1 = getOrder(1L, orderTable.getId(), OrderStatus.COOKING.name(), null, new ArrayList<>());
        Order order2 = getOrder(2L, orderTable.getId(), OrderStatus.COOKING.name(), null, new ArrayList<>());
        List<Order> orders = List.of(order1, order2);

        given(orderDao.findAll())
                .willReturn(orders);

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void changeOrderStatus() {
        // given
        OrderTable orderTable = getOrderTable(1L, 1L, 3, false);
        Order savedOrder = getOrder(1L, orderTable.getId(), OrderStatus.COOKING.name(), null, new ArrayList<>());
        Order updatedOrder = getOrder(1L, orderTable.getId(), OrderStatus.COMPLETION.name(), null, new ArrayList<>());
        OrderLineItem orderLineItem = getOrderLineItem(1L, savedOrder.getId(), 1L, 3);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        savedOrder.setOrderLineItems(orderLineItems);
        updatedOrder.setOrderLineItems(orderLineItems);

        given(orderDao.findById(savedOrder.getId()))
                .willReturn(Optional.of(savedOrder));
        given(orderLineItemDao.findAllByOrderId(savedOrder.getId()))
                .willReturn(orderLineItems);

        // when
        Order actual = orderService.changeOrderStatus(savedOrder.getId(), updatedOrder);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
