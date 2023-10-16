package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void throw_when_try_to_group_filled_order_table() {
        // given
        final boolean notEmptyStatus = false;
        final List<OrderTable> orderTables = List.of(
                new OrderTable(null, 1, true),
                new OrderTable(null, 3, notEmptyStatus)
        );
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> tableGroup.groupOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot group non-empty table or already grouped table.");
    }

    @Test
    void throw_when_try_to_group_already_grouped_order_table() {
        // given
        final List<OrderTable> orderTables = List.of(
                new OrderTable(new TableGroup(1L, null), 1, true),
                new OrderTable(new TableGroup(1L, null), 3, true)
        );
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> tableGroup.groupOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot group non-empty table or already grouped table.");
    }

    @Test
    void throw_when_try_to_group_under_two_order_tables() {
        // given
        final List<OrderTable> orderTables = List.of(
                new OrderTable(new TableGroup(1L, null), 1, true)
        );
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> tableGroup.groupOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Table group must have at least two tables.");
    }

    @Test
    void grouping_order_table() {
        // given
        final List<OrderTable> orderTables = List.of(
                new OrderTable(null, 1, true),
                new OrderTable(null, 3, true)
        );
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when
        tableGroup.groupOrderTables(orderTables);

        // then
        assertThat(tableGroup.getOrderTables())
                .containsExactlyElementsOf(orderTables);
    }
}
