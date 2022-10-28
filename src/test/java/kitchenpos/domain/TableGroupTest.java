package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @DisplayName("정상적인 경우 단체로 지정할 수 있다.")
    @Test
    void groupTable() {
        final TableGroup sut = new TableGroup(1L, LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(10, true);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        sut.group(orderTables, 2);

        assertAll(
                () -> assertThat(sut.getOrderTables()).hasSize(2),
                () -> sut.getOrderTables()
                        .forEach(orderTable -> assertThat(orderTable.getTableGroup().getId()).isEqualTo(sut.getId()))
        );
    }

    @DisplayName("존재하지 않거나 중복된 테이블을 단체 지정할 수 없다.")
    @Test
    void groupTableWithNotExistTableOrDuplicateTable() {
        final TableGroup sut = new TableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(10, true);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        assertThatThrownBy(() -> sut.group(orderTables, 3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아니면 단체로 지정할 수 없다.")
    @Test
    void groupTableWithNotEmptyTable() {
        final TableGroup sut = new TableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(5, false);
        final OrderTable orderTable2 = new OrderTable(10, false);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        assertThatThrownBy(() -> sut.group(orderTables, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체로 지정된 테이블인 경우 단체로 지정할 수 없다.")
    @Test
    void groupAlreadyGroupingTable() {
        final TableGroup sut = new TableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(5, true);
        orderTable1.groupTable(new TableGroup(1L, LocalDateTime.now()));
        final OrderTable orderTable2 = new OrderTable(10, true);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        assertThatThrownBy(() -> sut.group(orderTables, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroupTable() {
        final TableGroup sut = new TableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(10, true);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        sut.group(orderTables, 2);

        sut.ungroup();

        assertAll(
                () -> assertThat(sut.getOrderTables()).isEmpty(),
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }
}
