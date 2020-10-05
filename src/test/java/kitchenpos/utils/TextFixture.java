package kitchenpos.utils;

import java.util.Arrays;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TextFixture {

    public static OrderTable getOrderTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        return orderTable;
    }

    public static TableGroup getTableGroup(final OrderTable orderTable1, final OrderTable orderTable2) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        return tableGroup;
    }
}
