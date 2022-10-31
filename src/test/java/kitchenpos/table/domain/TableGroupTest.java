package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    @DisplayName("이미 단체 지정된 테이블을 포함해서 단체 지정할 수 없다")
    void setId() {
        // given
        OrderTable orderTable = new OrderTable(99, false);
        OrderTable orderTable2 = new OrderTable(99, false);
        TableGroup tableGroup = new TableGroup(List.of(orderTable, orderTable2));

        // when, then
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable, orderTable2)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다")
    void ungroup() {
        // given
        OrderTable orderTable = new OrderTable(99, false);
        OrderTable orderTable2 = new OrderTable(99, false);
        TableGroup tableGroup = new TableGroup(new ArrayList<>(List.of(orderTable, orderTable2)));

        // when
        tableGroup.ungroup();

        // then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables()).isEmpty(),
            () -> assertThat(orderTable.getTableGroup()).isNull()
        );
    }
}
