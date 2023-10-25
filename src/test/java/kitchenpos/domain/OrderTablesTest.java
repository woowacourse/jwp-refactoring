package kitchenpos.domain;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.exception.orderTableException.InvalidOrderTableException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTablesTest {

    @Test
    void create() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);

        //when&then
        assertDoesNotThrow(() -> new OrderTables(List.of(orderTable, orderTable2)));
    }

    @Test
    void validateCreate() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);

        //when&then
        assertThatThrownBy(() -> new OrderTables(List.of(orderTable)))
                .isInstanceOf(InvalidOrderTableException.class)
                .hasMessage("잘못된 OrderTable 입니다.");
    }

    @Test
    void ungroup() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);

        //when
        orderTable.ungroup();

        //then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }
}
