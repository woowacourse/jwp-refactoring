package kitchenpos;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TestObjectFactory {
    public static OrderTable creatOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable createChangeEmptyOrderTableDto(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static Order createOrder(Long orderTableId, String orderStatus, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(items);
        return order;
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
