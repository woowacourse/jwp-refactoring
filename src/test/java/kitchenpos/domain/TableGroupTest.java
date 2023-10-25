package kitchenpos.domain;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.tableGroup.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupTest {

    @Test
    void create() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable, orderTable2));

        //when&then
        assertDoesNotThrow(() -> new TableGroup(orderTables));
    }

    @Test
    void changeTableGroup() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable, orderTable2));

        //when
        final TableGroup tableGroup = new TableGroup(orderTables);

        //when&then
        assertAll(
                () -> Assertions.assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> Assertions.assertThat(orderTable2.getTableGroupId()).isEqualTo(tableGroup.getId())
        );
    }
}
