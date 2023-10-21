package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
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
    void throw_when_add_other_order_tables_order() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, false);
        final OrderTable otherOrderTable = new OrderTable(112312L, null, 1, false);
        final Order orderFromOtherOrderTable = new Order(otherOrderTable);

        // when & then
        assertThatThrownBy(() -> orderTable.addOrder(orderFromOtherOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order from other table is not allowed");
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
    void throw_when_try_to_change_empty_status_not_completed_table() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, false);
        orderTable.addOrder(new Order(1L, orderTable, OrderStatus.COOKING, null));

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot change empty status of table with order status not completion");
    }

    @Test
    void throw_when_try_to_change_number_of_guest_to_under_zero() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Number of guests must be greater than 0");
    }

    @Test
    void throw_when_try_to_change_number_of_guest_of_empty_table() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot change number of guests of empty table");
    }

    @Test
    void change_number_of_guests() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 1, false);

        // when
        orderTable.changeNumberOfGuests(3);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }
}
