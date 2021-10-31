package kitchenpos.generator;

import kitchenpos.domain.OrderTable;

public class TableGenerator {

    public static OrderTable newInstance(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable newInstance(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable newInstance(int numberOfGuests, boolean empty) {
        return newInstance(null, numberOfGuests, empty);
    }

    public static OrderTable newInstance(Long tableGroupId, int numberOfGuests, boolean empty) {
        return newInstance(null, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable newInstance(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
