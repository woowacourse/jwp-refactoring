package kitchenpos.tablegroup.vo;

import static kitchenpos.table.domain.OrderTableFixture.단체_지정_없는_주문_테이블;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GroupTablesTest {

    @Test
    void 주문_테이블_ID_목록을_반환한다() {
        // given
        Long orderTableId = 1L;
        GroupTables groupTables = new GroupTables(List.of(단체_지정_없는_주문_테이블(orderTableId)));

        // when
        List<Long> orderTableIds = groupTables.getOrderTableIds();

        // then
        Assertions.assertThat(orderTableIds).usingRecursiveComparison()
                .isEqualTo(List.of(orderTableId));
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        Long orderTableId = 1L;
        Long tableGroupId = 1L;
        OrderTable orderTable = 단체_지정_주문_테이블(orderTableId, tableGroupId);
        GroupTables groupTables = new GroupTables(List.of(orderTable));

        // when
        groupTables.ungroup();

        // then
        assertThat(orderTable.isGrouped()).isFalse();
    }
}
