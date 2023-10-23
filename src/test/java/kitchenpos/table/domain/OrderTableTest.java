package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import(TableValidator.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTableTest {

    private final TableValidator tableValidator = new TableValidator();

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
        final OrderTable orderTable = new OrderTable(new TableGroup(), 1, true);

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
        final TableGroup tableGroup = new TableGroup();

        // when
        orderTable.groupByTableGroup(tableGroup);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void throw_when_try_to_change_empty_status_of_grouped_table() {
        // given
        final OrderTable orderTable = new OrderTable(1L, new TableGroup(), 1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot change empty status of table in group");
    }

    @Test
    void throw_when_try_to_change_number_of_guest_to_under_zero() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1, tableValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Number of guests must be greater than 0");
    }

    @Test
    void throw_when_try_to_change_number_of_guest_of_empty_table() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3, tableValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot change number of guests of empty table");
    }

    @Test
    void change_number_of_guests() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, false);

        // when
        orderTable.changeNumberOfGuests(3, tableValidator);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }
}
