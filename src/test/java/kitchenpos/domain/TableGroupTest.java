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

        // when & then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot group non-empty table or already grouped table.");
    }

    @Test
    void throw_when_try_to_group_already_grouped_order_table() {
        // given
        final List<OrderTable> orderTables = List.of(
                new OrderTable(null, 1, true),
                new OrderTable(null, 2, true)
        );
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        final List<OrderTable> alreadyGroupedOrderTables = List.of(
                new OrderTable(tableGroup, 3, true),
                new OrderTable(tableGroup, 4, true)
        );

        // when & then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), alreadyGroupedOrderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot group non-empty table or already grouped table.");
    }

    @Test
    void throw_when_try_to_group_under_two_order_tables() {
        // given
        final List<OrderTable> orderTables = List.of(new OrderTable(null, 1, true));

        // when & then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Table group must have at least two tables.");
    }

    @Test
    void initialize_with_order_tables() {
        // given
        final List<OrderTable> orderTables = List.of(
                new OrderTable(null, 1, true),
                new OrderTable(null, 3, true)
        );

        // when
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // then
        assertThat(tableGroup.getOrderTables()).map(OrderTable::getTableGroup)
                .containsOnly(tableGroup);
    }
}
