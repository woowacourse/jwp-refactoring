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
                createEmptyTable(1L),
                createEmptyTable(2L)
        );
        TableGroup group = new TableGroup(LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.bind(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 맺을 테이블은 2개 이상이어야 한다.")
    @Test
    void wantToGroupingOrderTablesMustOverTwoSize() {
        // arrange
        List<OrderTable> orderTables = List.of(
                createEmptyTable(1L)
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.bind(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 맺을 테이블은 빈 테이블이어야 한다.")
    @Test
    void orderTablesMustNotEmpty() {
        // arrange
        List<OrderTable> orderTables = List.of(
                createEmptyTable(1L),
                createOrderTable(2L)
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.bind(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 맺을 테이블은 기존 그룹이 없어야 한다.")
    @Test
    void orderTableMustNotGroupedTable() {
        // arrange
        List<OrderTable> orderTables = List.of(
                createGroupedOrderTable(1L, 2L),
                createEmptyTable(2L)
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act & assert
        assertThatThrownBy(() -> group.bind(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 그룹을 만든다.")
    @Test
    void groupOrderTables() {
        // arrange
        List<OrderTable> orderTables = List.of(
                createEmptyTable(1L),
                createEmptyTable(2L)
        );
        TableGroup group = new TableGroup(1L, LocalDateTime.now());

        // act
        group.bind(orderTables);

        // assert
        assertThat(orderTables)
                .extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                .containsExactly(
                        Tuple.tuple(1L, false),
                        Tuple.tuple(1L, false)
                );
    }

    private OrderTable createEmptyTable(final Long id) {
        return new OrderTable(id, null, 0, true, List.of());
    }

    private OrderTable createOrderTable(final Long id) {
        return new OrderTable(id, null, 0, false, List.of());
    }

    private OrderTable createGroupedOrderTable(final Long id, final Long groupId) {
        return new OrderTable(id, groupId, 0, false, List.of());
    }
}
