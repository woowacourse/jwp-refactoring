package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    @Test
    void 테이블_그룹을_생성할_수_있다() {
        OrderTable orderTable1 = new OrderTable(null, 2, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        assertDoesNotThrow(() -> new TableGroup(List.of(orderTable1, orderTable2)));
    }

    @Test
    void 그룹으로_지정할_테이블은_두_개_이상이어야_한다() {
        OrderTable orderTable = new OrderTable(null, 2, true);

        assertThatThrownBy(() -> new TableGroup(List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹으로_지정할_테이블은_비어있어야_한다() {
        OrderTable orderTable1 = new OrderTable(null, 2, false);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        assertThatThrownBy(() -> new TableGroup(List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_그룹으로_지정된_테이블은_그룹으로_지정할_수_없다() {
        OrderTable alreadyGroupedOrderTable1 = new OrderTable(1L, 2, true);
        OrderTable alreadyGroupedOrderTable2 = new OrderTable(1L, 2, true);

        assertThatThrownBy(() -> new TableGroup(List.of(alreadyGroupedOrderTable1, alreadyGroupedOrderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
