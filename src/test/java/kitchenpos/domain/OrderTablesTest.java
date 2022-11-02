package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void matchSize() {
        OrderTable orderTable1 = new OrderTable(null, 10, false);
        OrderTable orderTable2 = new OrderTable(null, 10, false);
        OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        assertThat(orderTables.matchSize(2)).isTrue();
    }

    @Test
    void validateOrderTables() {
        assertThatThrownBy(() -> new OrderTables(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블은 2 테이블 이상부터 가능합니다.");
    }
}
