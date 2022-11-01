package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("테이블 그룹을 만든다")
    @Test
    void group() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(4, true);
        tableGroup.group(List.of(orderTable1, orderTable2));

        List<OrderTable> actual = tableGroup.getOrderTables();
        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(actual).hasSize(2)
        );
    }

    @DisplayName("2개 미만의 테이블로 그룹화하면 예외가 발생한다")
    @Test
    void validateOrderTableSize() {
        TableGroup tableGroup = new TableGroup();

        List<OrderTable> orderTables = List.of();
        assertThatThrownBy(() -> tableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블을 그룹화하려면 2개 이상의 테이블이 필요합니다.");
    }

    @DisplayName("비어있지 않은 테이블을 그룹화하면 예외가 발생한다")
    @Test
    void validateTableIsEmpty() {
        boolean tableStatus = false;
        TableGroup tableGroup = new TableGroup();

        List<OrderTable> orderTables = List.of(new OrderTable(3, tableStatus), new OrderTable(4, true));

        assertThatThrownBy(() -> tableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블이 비어있지 않습니다.");
    }

    @DisplayName("이미 그룹이 형성된 테이블로 그룹화하면 예외가 발생한다")
    @Test
    void validateInNotGroupedTable() {
        OrderTable alreadyGroupedTable = new OrderTable(3, false);
        alreadyGroupedTable.joinTableGroup(new TableGroup());

        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = List.of(alreadyGroupedTable, new OrderTable(4, false));

        assertThatThrownBy(() -> tableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 테이블 그룹이 형성된 테이블입니다.");
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(4, true);
        tableGroup.group(List.of(orderTable1, orderTable2));

        tableGroup.ungroup();

        List<OrderTable> actual = tableGroup.getOrderTables();
        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull(),
                () -> assertThat(actual).hasSize(0)
        );
    }
}
