package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    @DisplayName("테이블 개수가 의도한 개수가 맞는지 반환한다")
    void hasValidOrderTables() {
        final OrderTables orderTables = generateOrderTables();

        final boolean actual = orderTables.hasValidOrderTableSize(generateOrderTables().getOrderTables());

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("테이블들이 테이블 그룹에 포함될 수 있는지 여부를 반환한다")
    void isGroupAble() {
        final OrderTables orderTables = generateOrderTables();

        final boolean actual = orderTables.isGroupAble();

        assertThat(actual).isTrue();
    }

    private OrderTables generateOrderTables() {
        final List<OrderTable> tables = new ArrayList<>();
        final OrderTable orderTable1 = new OrderTable(2, true);
        tables.add(orderTable1);
        final OrderTable orderTable2 = new OrderTable(2, true);
        tables.add(orderTable2);

        return new OrderTables(tables);
    }
}
