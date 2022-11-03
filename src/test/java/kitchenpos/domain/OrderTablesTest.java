package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTablesTest {

    @DisplayName("정상적인 경우 단체로 지정할 수 있다.")
    @Test
    void groupTable() {
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(10, true);
        final OrderTables sut = new OrderTables(List.of(orderTable1, orderTable2));

        sut.group(1L, 2);

        assertAll(
                () -> assertThat(sut.getOrderTables()).hasSize(2),
                () -> sut.getOrderTables()
                        .forEach(orderTable -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L))
        );
    }

    @DisplayName("존재하지 않거나 중복된 테이블을 단체 지정할 수 없다.")
    @Test
    void groupTableWithNotExistTableOrDuplicateTable() {
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(10, true);
        final OrderTables sut = new OrderTables(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> sut.group(1L, 3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아니면 단체로 지정할 수 없다.")
    @Test
    void groupTableWithNotEmptyTable() {
        final OrderTable orderTable1 = new OrderTable(5, false);
        final OrderTable orderTable2 = new OrderTable(10, false);
        final OrderTables sut = new OrderTables(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> sut.group(1L, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체로 지정된 테이블인 경우 단체로 지정할 수 없다.")
    @Test
    void groupAlreadyGroupingTable() {
        final OrderTable orderTable1 = new OrderTable(5, true);
        orderTable1.groupTable(1L);
        final OrderTable orderTable2 = new OrderTable(10, true);
        final OrderTables sut = new OrderTables(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> sut.group(1L, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroupTable() {
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(10, true);
        final OrderTables sut = new OrderTables(List.of(orderTable1, orderTable2));
        sut.group(1L, 2);

        sut.ungroup();

        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
