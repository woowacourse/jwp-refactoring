package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
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
                () -> new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2))
        );
    }

    @Test
    void 테이블_그룹_생성_시_테이블_수가_2개_미만이면_예외_발생() {
        final OrderTable orderTable = new OrderTable(null, 0, true);

        assertThatThrownBy(
                () -> new TableGroup(LocalDateTime.now(), List.of(orderTable))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("order table 수는 2 이상이어야 합니다.");
    }

    @Test
    void 테이블_그룹_생성_시_테이블이_비어있지_않으면_예외_발생() {
        final OrderTable orderTable = new OrderTable(null, 0, false);
        final OrderTable orderTable2 = new OrderTable(null, 0, false);

        assertThatThrownBy(
                () -> new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("orderTable 은 비어있어야 하고, 소속된 table group이 없어야 합니다.");
    }

    @Test
    void 테이블_그룹_생성_시_테이블이_이미_속한_그룹이_있으면_예외_발생() {
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTable orderTable2 = new OrderTable(new TableGroup(), 0, true);

        assertThatThrownBy(
                () -> new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("orderTable 은 비어있어야 하고, 소속된 table group이 없어야 합니다.");
    }

    @Test
    void 테이블이_속한_그룹을_해당_테이블_그룹으로_변경() {
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTable orderTable2 = new OrderTable(null, 0, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2));

        assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
    }

    @Test
    void 테이블_그룹_해제() {
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final OrderTable orderTable2 = new OrderTable(null, 0, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2));

        tableGroup.ungroup();

        assertSoftly(softly -> {
                    assertThat(tableGroup.getOrderTables()).isNull();
                    assertThat(orderTable.getTableGroup()).isNull();
                    assertThat(orderTable2.getTableGroup()).isNull();
                }
        );
    }
}
