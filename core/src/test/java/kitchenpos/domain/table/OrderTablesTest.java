package kitchenpos.domain.table;

import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTables;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @Test
    void 생성할_때_OrderTable이_두_개_이상이면_성공한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(null, 5, true);
        final OrderTable orderTable2 = new OrderTable(null, 5, true);

        // when, then
        assertThatCode(() -> OrderTables.from(List.of(orderTable1, orderTable2)))
                .doesNotThrowAnyException();
    }

    @Test
    void 생성할_때_OrderTable이_두_개_미만이면_실패한다() {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, true);

        // when, then
        assertThatThrownBy(() -> OrderTables.from(List.of(orderTable)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 생성할_때_OrderTable이_비어있으면_실패한다() {
        // when, then
        assertThatThrownBy(() -> OrderTables.from(List.of()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 생성할_때_OrderTable이_null이면_실패한다() {
        // when, then
        assertThatThrownBy(() -> OrderTables.from(null))
                .isInstanceOf(IllegalStateException.class);
    }
}
