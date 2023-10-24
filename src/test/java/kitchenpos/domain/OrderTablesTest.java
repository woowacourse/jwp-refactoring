package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(2, true);
        final OrderTable orderTable2 = new OrderTable(3, true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
        final TableGroup tableGroup = new TableGroup(orderTables);
        orderTable1.updateTableGroup(tableGroup);
        orderTable2.updateTableGroup(tableGroup);

        // when
        orderTables.ungroup();

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }
}
