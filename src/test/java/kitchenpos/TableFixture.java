package kitchenpos;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.tableGroup.TableGroup;

import java.time.LocalDateTime;

public class TableFixture {

    private static final int NUMBER_OF_GUEST = 10;
    private static final LocalDateTime CREATE_DATE = LocalDateTime.now();

    public static OrderTable createOrderTable() {
        return createOrderTable(null);
    }

    public static OrderTable createOrderTable(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(NUMBER_OF_GUEST);
        orderTable.setEmpty(false);
        orderTable.setId(id);
        return orderTable;
    }

    public static TableGroup createTableGroup() {
        return createTableGroup(null);
    }

    public static TableGroup createTableGroup(Long id) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(CREATE_DATE);
        return tableGroup;
    }
}
