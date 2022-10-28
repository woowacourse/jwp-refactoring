package kitchenpos.application;

import static kitchenpos.support.OrderFixture.ORDER_COMPLETION_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_EMPTY_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Test
    void 테이블그룹을_생성한다() {
        // given
        final OrderTable firstSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final OrderTable secondSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(firstSavedOrderTable, secondSavedOrderTable));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isEqualTo(1L);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_그룹이_2개이상이_아니면_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_그룹테이블이_존재하지_않으면_예외를_발생한다() {
        // given
        final OrderTable firstUnsavedOrderTable = ORDER_TABLE_EMPTY_1.생성();
        final OrderTable secondSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(firstUnsavedOrderTable, secondSavedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_테이블이_비어있지_않다면_예외를_발생한다() {
        // given
        final OrderTable fullSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        final OrderTable emptySavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(fullSavedOrderTable, emptySavedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_테이블중_이미_테이블_그룹이_있다면_예외를_발생한다() {
        // given
        final Long alreadySavedTableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable alreadySavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성(alreadySavedTableGroupId));

        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(alreadySavedOrderTable, savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제할_수_있다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable alreadySavedOrderTable1 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성(tableGroupId));
        final OrderTable alreadySavedOrderTable2 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성(tableGroupId));
        주문을_저장한다(ORDER_COMPLETION_1.주문항목_없이_생성(alreadySavedOrderTable1.getId()));
        주문을_저장한다(ORDER_COMPLETION_1.주문항목_없이_생성(alreadySavedOrderTable2.getId()));

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        final List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables)
                .extracting("tableGroupId")
                .containsExactly(null, null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 테이블_그룹을_해제할_때_주문테이블의_주문상태가_제조중이거나_식사중이면_예외를_발생한다(final String status) {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable alreadySavedOrderTable1 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성(tableGroupId));
        final OrderTable alreadySavedOrderTable2 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성(tableGroupId));
        주문을_저장한다(new Order(alreadySavedOrderTable1.getId(), OrderStatus.valueOf(status), LocalDateTime.now()));
        주문을_저장한다(ORDER_COMPLETION_1.주문항목_없이_생성(alreadySavedOrderTable2.getId()));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
