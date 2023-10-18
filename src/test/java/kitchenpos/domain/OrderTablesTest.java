package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTablesTest {

    @Test
    void validateSize() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> orderTables.validateSize(2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateIsEmptyAndTableGroupIdIsNullIfIsNotEmpty() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(orderTables::validateIsEmptyAndTableGroupIdIsNull)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateIsEmptyAndTableGroupIdIsNullIfTableGroupIsNotNull() {
        //given
        final OrderTable orderTable = new OrderTable(1L, 0, true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(orderTables::validateIsEmptyAndTableGroupIdIsNull)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTableGroupIdAndEmpty() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        //when
        orderTables.updateTableGroupIdAndEmpty(1L, false);

        //then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void mapOrderTableIds() {
        //given
        final OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        //when
        final List<Long> results = orderTables.mapOrderTableIds();

        //then
        assertThat(results).contains(1L);
    }
}
