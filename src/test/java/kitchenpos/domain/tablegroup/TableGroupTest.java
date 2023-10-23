package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void 단체_지정을_위해_테이블_그룹을_생성한다() {
        // expect
        assertThatNoException().isThrownBy(TableGroup::create);
    }

    @Test
    void 단체_지정을_하는_경우_테이블이_두_개_이상이_아닌_경우_예외를_던진다() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable orderTable = new OrderTable(0, true);

        // expect
        assertThatThrownBy(() -> tableGroup.changeOrderTables(List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정하려는 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    void 단체_지정을_성공한다() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);

        // when
        tableGroup.changeOrderTables(List.of(orderTable1, orderTable2));

        // then
        assertThat(tableGroup.getOrderTables()).containsExactly(orderTable1, orderTable2);
    }
}
