package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TableGroupFixture {

    public static TableGroup createEmptyOrderTable() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(null);
        OrderTable orderTable = new OrderTable();
        tableGroup.setOrderTables(Arrays.asList(orderTable));

        return tableGroup;
    }

    public static TableGroup createTableGroupWithOrderTableSize(int count) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(OrderTableFixture.createOrderTableCountBy(count));

        return tableGroup;
    }

    public static TableGroup createTableGroupWithNotEmptyOrderTableSize(int count) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(null);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(OrderTableFixture.createOrderTableCountBy(count));

        return tableGroup;
    }

    public static TableGroup createWithId(long id) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroup;
    }
}