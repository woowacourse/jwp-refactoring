package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void create() {
        OrderTable orderTable1 = OrderTable.EMPTY_TABLE;
        OrderTable orderTable2 = OrderTable.EMPTY_TABLE;
        makeResponse("/api/tables", TestMethod.POST, orderTable1)
            .as(OrderTable.class);
        makeResponse("/api/tables", TestMethod.POST, orderTable2)
            .as(OrderTable.class);

        List<OrderTable> orderTables = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", OrderTable.class);

        TableGroup tableGroup = new TableGroup(orderTables);
        TableGroup created = makeResponse("/api/table-groups", TestMethod.POST, tableGroup)
            .as(TableGroup.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getOrderTables()).isNotEmpty()
        );
    }

    @Test
    void ungroup() {
        OrderTable orderTable1 = OrderTable.EMPTY_TABLE;
        OrderTable orderTable2 = OrderTable.EMPTY_TABLE;
        makeResponse("/api/tables", TestMethod.POST, orderTable1)
            .as(OrderTable.class);
        makeResponse("/api/tables", TestMethod.POST, orderTable2)
            .as(OrderTable.class);

        List<OrderTable> orderTables = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", OrderTable.class);

        TableGroup tableGroup = new TableGroup(orderTables);
        TableGroup created = makeResponse("/api/table-groups", TestMethod.POST, tableGroup)
            .as(TableGroup.class);

        makeResponse("/api/table-groups/" + created.getId(), TestMethod.DELETE);

        List<OrderTable> ungroupedTables = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", OrderTable.class);
        boolean actual = ungroupedTables.stream()
            .noneMatch(table -> created.getId().equals(table.getTableGroupId()));
        assertThat(actual).isTrue();
    }
}