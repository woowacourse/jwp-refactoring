package kitchenpos.application.common;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class TestObjectFactory {
    public static OrderTable creatOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.changeNumberOfGuests(0);
        orderTable.changeEmpty(true);
        return orderTable;
    }

    public static OrderTable createChangeEmptyOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(empty);
        return orderTable;
    }

    public static OrderTable createChangeNumberOfGuestsDto(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.changeNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static Order createChangeOrderStatusDto(OrderStatus orderStatus) {
        Order order = new Order();
        order.changeOrderStatus(orderStatus);
        return order;
    }
}
