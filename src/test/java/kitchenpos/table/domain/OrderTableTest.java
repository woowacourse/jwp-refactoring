package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void group_filled_order_table_is_not_possible() {
        // given
        final OrderTable orderTable = new OrderTable(null, 1, false);

        // when
        final boolean ableToGroup = orderTable.isAbleToGroup();

        // then
        assertThat(ableToGroup).isFalse();
    }

    @Test
    void group_already_grouped_order_table_is_not_possible() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 1, true);

        // when
        final boolean ableToGroup = orderTable.isAbleToGroup();

        // then
        assertThat(ableToGroup).isFalse();
    }

    @Test
    void is_able_to_group() {
        // given
        final OrderTable orderTable = new OrderTable(null, 1, true);

        // when
        final boolean ableToGroup = orderTable.isAbleToGroup();

        // then
        assertThat(ableToGroup).isTrue();
    }

    @Test
    void filled_after_grouped_by_table_group() {
        // given
        final OrderTable orderTable = new OrderTable(null, 1, true);

        // when
        orderTable.groupByTableGroup(1L);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }
}
