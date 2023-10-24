package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @Test
    void OrderTables를_생성한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);

        // when
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // then
        assertThat(orderTables.getOrderTables()).isEqualTo(List.of(orderTable1, orderTable2));
    }

    @Test
    void OrderTables_생성_시_받은_OrderTable_중_빈_상태가_아닌_테이블이_있다면_예외가_발생한다() {
        // given
        final OrderTable emptyOrderTable = new OrderTable(0, false);
        final OrderTable notEmptyOrderTable = new OrderTable(0, true);

        // when, then
        assertThatThrownBy(() -> new OrderTables(List.of(emptyOrderTable, notEmptyOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTables_생성_시_받은_OrderTable_중_다른_TableGroup에_속한_테이블이_있다면_예외가_발생한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable hasGroupOrderTable = new OrderTable(1L, tableGroup, 0, false);
        final OrderTable notGroupOrderTable = new OrderTable(0, true);

        // when, then
        assertThatThrownBy(() -> new OrderTables(List.of(hasGroupOrderTable, notGroupOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTables의_테이블들의_아이디_목록을_반환한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 0, true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        final List<Long> orderTableIds = orderTables.getOrderTableIds();

        // then
        assertThat(orderTableIds).containsExactly(1L, 2L);
    }

    @Test
    void OrderTables의_테이블들이_속한_테이블그룹을_해제한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 0, true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
        final TableGroup tableGroup = new TableGroup();
        orderTables.registerGroup(tableGroup);

        // when
        orderTables.leaveGroup();
        final List<OrderTable> orderTableList = orderTables.getOrderTables();

        // then
        for (OrderTable orderTable : orderTableList) {
            assertThat(orderTable.getTableGroup()).isNull();
        }
    }
}