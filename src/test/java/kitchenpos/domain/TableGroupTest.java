package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {

    @Test
    void create(){
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable, orderTable2));


        //when&then
        assertDoesNotThrow(() -> new TableGroup(orderTables));
    }

    @Test
    void changeTableGroup(){
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable, orderTable2));

        //when
        final TableGroup tableGroup = new TableGroup(orderTables);

        //when&then
        assertAll(
                () -> Assertions.assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup),
                () -> Assertions.assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup)
        );
    }
}
