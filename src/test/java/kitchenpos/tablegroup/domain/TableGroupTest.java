package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void 테이블_그룹_생성() {
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTable orderTable2 = new OrderTable(null, 0, true);

        assertDoesNotThrow(
                () -> new TableGroup(List.of(orderTable, orderTable2))
        );
    }

    @Test
    void 테이블이_속한_그룹을_해당_테이블_그룹으로_변경() {
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTable orderTable2 = new OrderTable(null, 0, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable, orderTable2));

        assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroup.getId());
    }

    @Test
    void 테이블_그룹_해제() {
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTable orderTable2 = new OrderTable(null, 0, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable, orderTable2));

        tableGroup.ungroup();

        assertSoftly(softly -> {
                    assertThat(tableGroup.getOrderTables()).isNull();
            assertThat(orderTable.getTableGroupId()).isNull();
            assertThat(orderTable2.getTableGroupId()).isNull();
                }
        );
    }
}
