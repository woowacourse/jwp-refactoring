package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.common.UnitTest;
import kitchenpos.table.OrderTable;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderTableTest {

    @Test
    void orderTable을_생성한다() {
        OrderTable actual = new OrderTable(1L, 0, true);

        assertAll(() -> {
            assertThat(actual.getTableGroupId()).isEqualTo(1L);
            assertThat(actual.getNumberOfGuests()).isEqualTo(0);
            assertThat(actual.isEmpty()).isTrue();
        });
    }

    @Test
    void orderTable을_group한다() {
        OrderTable orderTable = new OrderTable(null, 0, true);

        orderTable.group(1L);

        assertAll(() -> {
            assertThat(orderTable.getTableGroupId()).isEqualTo(1L);
            assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Test
    void orderTable을_ungroup한다() {
        OrderTable orderTable = new OrderTable(1L, 0, false);

        orderTable.ungroup();

        assertAll(() -> {
            assertThat(orderTable.getTableGroupId()).isNull();
            assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Test
    void orderTable을_changeEmpty한다() {
        OrderTable orderTable = new OrderTable(null, 0, false);

        orderTable.changeEmpty(() -> false, true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void orderTable을_changeEmpty할_때_tableGroupId가_null이_아니면_예외를_던진다() {
        OrderTable orderTable = new OrderTable(1L, 0, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(() -> false, true));
    }

    @Test
    void orderTable을_changeEmpty할_때_특정_조건이_true이면_예외를_던진다() {
        OrderTable orderTable = new OrderTable(null, 0, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(() -> true, true));
    }
}
