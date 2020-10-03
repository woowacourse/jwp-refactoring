package kitchenpos;

import kitchenpos.domain.OrderTable;

public class TestObjectFactory {
    public static OrderTable creatOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTable;
    }
}
