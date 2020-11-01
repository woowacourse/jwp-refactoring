package kitchenpos.application;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    static OrderTable createOrderTable(Long id, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
