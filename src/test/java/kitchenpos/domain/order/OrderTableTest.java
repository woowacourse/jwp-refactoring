package kitchenpos.domain.order;

import kitchenpos.domain.DomainTest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest extends DomainTest {
    @Test
    void throw_when_number_of_guests_is_below_zero() {
        // when & then
        assertThatThrownBy(() -> OrderTable.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTable.NUMBER_OF_GUESTS_IS_BELOW_ZERO_ERROR_MESSAGE);
    }

    @Test
    void throw_when_change_unorderable_table_number_of_table() {
        // given
        final OrderTable orderTable = OrderTable.of(0);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTable.CHANGE_UNORDERABLE_TABLE_NUMBER_OF_TABLE_ERROR_MESSAGE);
    }

    @Test
    void throw_when_change_unorderable_table_when_in_table_group() {
        // given
        final OrderTable orderTable = OrderTable.of(TableGroup.of(List.of(OrderTable.of(0), OrderTable.of(0))));

        // when & then
        assertThatThrownBy(orderTable::setUnOrderable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTable.CHANGE_UNORDERABLE_TABLE_WHEN_IN_TABLE_GROUP_ERROR_MESSAGE);
    }
}