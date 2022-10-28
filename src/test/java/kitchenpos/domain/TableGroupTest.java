package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("그룹의 ID는 존재해야 한다.")
    @Test
    void tableGroupIdMustExist() {
        // arrange
        List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, 0, true, List.of()),
                new OrderTable(2L, null, 0, true, List.of())
        );
        TableGroup group = new TableGroup(LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.union(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 맺을 테이블은 2개 이상이어야 한다.")
    @Test
    void wantToGroupingOrderTablesMustOverTwoSize() {
        // arrange
        List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, 0, true, List.of())
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.union(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 맺을 테이블은 빈 테이블이어야 한다.")
    @Test
    void orderTablesMustNotEmpty() {
        // arrange
        List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, 0, true, List.of()),
                new OrderTable(2L, null, 2, false, List.of())
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.union(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 맺을 테이블은 기존 그룹이 없어야 한다.")
    @Test
    void orderTableMustNotGroupedTable() {
        // arrange
        List<OrderTable> orderTables = List.of(
                new OrderTable(1L, 2L, 0, true, List.of()),
                new OrderTable(2L, null, 2, true, List.of())
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.union(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 그룹을 만든다.")
    @Test
    void groupOrderTables() {
        // arrange
        List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, 0, true, List.of()),
                new OrderTable(2L, null, 0, true, List.of())
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act
        group.union(orderTables);

        // assert
        assertThat(orderTables)
                .extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                .containsExactly(
                        Tuple.tuple(1L, false),
                        Tuple.tuple(1L, false)
                );
    }
}
