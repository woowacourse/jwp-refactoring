package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTestBase {

    @Test
    void 테이블_그룹_정상_생성() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable orderTable2 = 빈_주문_테이블_생성();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        Optional<TableGroup> actual = tableGroupDao.findById(savedTableGroup.getId());
        assertThat(actual).isNotEmpty();
    }
}
