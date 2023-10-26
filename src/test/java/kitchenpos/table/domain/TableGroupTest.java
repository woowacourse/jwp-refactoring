package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupTest {

    @DisplayName("테이블 그룹에 주문 테이블을 추가하면 연관관계가 맺어진다.")
    @Test
    void add_orderTable() {
        // given
        final OrderTable orderTable = new OrderTable(2, false);
        final TableGroup tableGroup = new TableGroup(2L);

        // when
        tableGroup.addOrderTable(orderTable);

        // then
        assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
    }

    @DisplayName("테이블 그룹에 주문 테이블을 제외하면 연관관계가 끊어진다.")
    @Test
    void upGroup_orderTable() {
        // given
        final OrderTable orderTable = new OrderTable(2, false);
        final TableGroup tableGroup = new TableGroup(2L);
        tableGroup.addOrderTable(orderTable);

        // when
        tableGroup.ungroup();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
        assertThat(orderTable.isEmpty()).isTrue();
    }
}
