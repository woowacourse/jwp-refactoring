package kitchenpos.application.common;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class TestObjectFactory {
    public static OrderTable creatOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable createChangeEmptyOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createChangeNumberOfGuestsDto(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static Order createOrder(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderTable(orderTable);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(items);
        return order;
    }

    public static Order createChangeOrderStatusDto(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        return order;
    }
}
