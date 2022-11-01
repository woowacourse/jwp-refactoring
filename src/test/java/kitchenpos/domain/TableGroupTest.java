package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Test
    void 테이블_그룹을_초기화한다() {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        final TableGroup actual = tableGroup.init(orderTables);

        // then
        final TableGroup expected = new TableGroup(tableGroup.getId(), LocalDateTime.now(),
                orderTables.getOrderTables());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("createdDate")
                .isEqualTo(expected);
    }
}
