package kitchenpos.domain;

import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("테이블 목록은 비어있을 수 없다")
    @Test
    void orderTablesIsEmpty_throwsException() {
        final List<OrderTable> emptyOrderTables = Collections.emptyList();

        assertThatThrownBy(
                () -> new TableGroup(LocalDateTime.now(), emptyOrderTables)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록은 크기가 1이 될 수 없다")
    @Test
    void orderTablesSizeIsOne_throwsException() {
        final var table = emptyTable(1);
        final var oneInOrderTables = List.of(table);

        assertThatThrownBy(
                () -> new TableGroup(LocalDateTime.now(), oneInOrderTables)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 중 하나라도 빈 상태가 아니라면, 그룹을 만들 수 없다")
    @Test
    void notEmptyTableInOrderTables_throwsException() {
        final var emptyTable = emptyTable(2);
        final var notEmptyTable = notEmptyTable(2);

        assertThatThrownBy(
                () -> new TableGroup(LocalDateTime.now(), List.of(emptyTable, notEmptyTable))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 중 하나라도 이미 그룹 아이디를 갖고 있다면, 그룹을 만들 수 없다")
    @Test
    void tableContainsGroupIdExists_throwsException() {
        final var otherGroupTableId = 1L;
        final var otherGroupedTable = new OrderTable(1L, otherGroupTableId, 2, true);

        final var emptyTable = emptyTable(2);

        assertThatThrownBy(
                () -> new TableGroup(LocalDateTime.now(), List.of(otherGroupedTable, emptyTable))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
