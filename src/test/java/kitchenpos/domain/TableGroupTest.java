package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @DisplayName("비어 있지 않은 테이블로 단체 지정을 하면 예외가 발생한다.")
    @Test
    void from_ContainsNotEmptyTable_ExceptionThrown() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 5, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 5, false);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        // when, then
        assertThatThrownBy(() -> TableGroup.from(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정된 테이블을 단체 지정 하면 예외가 발생한다.")
    @Test
    void from_AlreadyGroupedTable_ExceptionThrown() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 5, true);
        OrderTable orderTable2 = new OrderTable(2L, 2L, 5, true);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        // when, then
        assertThatThrownBy(() -> TableGroup.from(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹에 테이블들을 할당한다.")
    @Test
    void assignTables() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 5, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 5, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), null);

        // when
        tableGroup.assignTables(List.of(orderTable1, orderTable2));

        // then
        assertThat(tableGroup.getOrderTables()).allMatch(orderTable -> orderTable.getTableGroupId().equals(tableGroup.getId()));
    }
}
