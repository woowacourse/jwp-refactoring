package kitchenpos;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;

import java.time.LocalDateTime;

public class TableFixture {

    private static final int NUMBER_OF_GUEST = 10;
    private static final LocalDateTime CREATE_DATE = LocalDateTime.now();

    public static OrderTable createOrderTable() {
        return new OrderTable(NUMBER_OF_GUEST, false);
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
