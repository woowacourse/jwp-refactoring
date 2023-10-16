package kitchenpos.common.fixtures;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixtures {

    /**
     * ORDER_TABLES
     */
    public static List<OrderTable> TABLE_GROUP1_ORDER_TABLES() {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        return List.of(orderTable1, orderTable2);
    }

    /**
     * REQUEST
     */
    public static TableGroup TABLE_GROUP1_CREATE_REQUEST() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = TABLE_GROUP1_ORDER_TABLES();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
