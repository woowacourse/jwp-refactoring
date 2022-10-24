package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
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

    @Test
    void 주문_테이블_1개로_테이블_그룹_생성_시_실패() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = 빈_주문_테이블_생성();
        tableGroup.setOrderTables(Collections.singletonList(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있지_않은_주문_테이블로_테이블_그룹_생성_시_실패() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = 주문_테이블_생성();
        OrderTable orderTable2 = 주문_테이블_생성();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
