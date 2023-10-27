package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @DisplayName("테이블들의 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 2L, 5, true);
        OrderTable orderTable2 = new OrderTable(2L, 2L, 5, true);
        OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        orderTables.ungroup();

        // then
        Assertions.assertAll(
                () -> assertThat(orderTables.getOrderTables()).allMatch(orderTable -> orderTable.getTableGroupId() == null),
                () -> assertThat(orderTables.getOrderTables()).allMatch(orderTable -> !orderTable.isEmpty())
        );

    }
}
