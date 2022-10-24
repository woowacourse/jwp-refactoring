package kitchenpos.application;

import static kitchenpos.support.OrderFixture.ORDER_COMPLETION_1;
import static kitchenpos.support.OrderFixture.ORDER_COOKING_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_EMPTY_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Test
    void 테이블이_저장될_때_테이블그룹이_null로_지정된다() {
        // given
        final OrderTable orderTable = ORDER_TABLE_NOT_EMPTY_1.생성(1L);

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @Test
    void 테이블을_비어있음_상태로_변경한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        주문을_저장한다(ORDER_COMPLETION_1.생성(savedOrderTable.getId()));

        final OrderTable updateFor = new OrderTable(null, savedOrderTable.getNumberOfGuests(), true);

        // when
        final OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), updateFor);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 대상테이블이_존재하지_않으면_테이블을_비어있음_상태로_변경할_수_없다() {
        // given
        final long notExistOrderTableId = Long.MAX_VALUE;

        final OrderTable updateFor = new OrderTable(null, 4, true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_아이디가_null이_아니면_테이블을_비어있음_상태로_변경할_수_없다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId));
        주문을_저장한다(ORDER_COMPLETION_1.생성(savedOrderTable.getId()));

        final OrderTable updateFor = new OrderTable(null, savedOrderTable.getNumberOfGuests(), true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태가_조리중이거나_식사중_상태이면_테이블을_비어있음_상태로_변경할_수_없다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        주문을_저장한다(ORDER_COOKING_1.생성(savedOrderTable.getId()));

        final OrderTable updateFor = new OrderTable(null, savedOrderTable.getNumberOfGuests(), true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_고객수를_변경할_수_있다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());

        final OrderTable updateFor = new OrderTable(null, 4, savedOrderTable.isEmpty());

        // when
        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), updateFor);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void 테이블의_고객수를_변경할_때_고객수가_음수이면_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());

        final OrderTable updateFor = new OrderTable(null, -1, savedOrderTable.isEmpty());

        // when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_고객수를_변경할_때_테이블이_존재하지_않으면_예외가_발생한다() {
        // given
        final long notExistOrderTableId = Long.MAX_VALUE;

        final OrderTable updateFor = new OrderTable(null, 4, true);

        // when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistOrderTableId, updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_고객수를_변경할_때_테이블이_비어있으면_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());

        final OrderTable updateFor = new OrderTable(null, 4, savedOrderTable.isEmpty());

        // when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
