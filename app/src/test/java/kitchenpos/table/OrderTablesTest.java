package kitchenpos.table;

import static kitchenpos.tablegroup.domain.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(2, true);
        final OrderTable orderTable2 = new OrderTable(3, true);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        테이블_그룹(null, null);

        // when
        final OrderTables savedOrderTables = new OrderTables(orderTables);
        savedOrderTables.ungroup();

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
