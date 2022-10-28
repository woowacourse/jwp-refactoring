package kitchenpos.application;

import static kitchenpos.support.OrderFixture.ORDER_COMPLETION_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_EMPTY_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableEmptyRequest;
import kitchenpos.dto.request.OrderTableNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Test
    void 테이블이_저장될_때_테이블그룹이_null로_지정된다() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);

        // when
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        // then

        assertThat(orderTableResponse.getTableGroupId()).isNull();
    }

    @Test
    void 테이블을_비어있음_상태로_변경한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        주문을_저장한다(ORDER_COMPLETION_1.주문항목_없이_생성(savedOrderTable.getId()));

        final OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);

        // when
        final OrderTableResponse orderTableResponse = tableService.changeEmpty(savedOrderTable.getId(),
                orderTableEmptyRequest);

        // then
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    @Test
    void 대상테이블이_존재하지_않으면_테이블을_비어있음_상태로_변경할_수_없다() {
        // given
        final long notExistOrderTableId = Long.MAX_VALUE;

        final OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, orderTableEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_아이디가_null이_아니면_테이블을_비어있음_상태로_변경할_수_없다() {
        // given
        final TableGroup tableGroup = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성());
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroup));
        주문을_저장한다(ORDER_COMPLETION_1.주문항목_없이_생성(savedOrderTable.getId()));

        final OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COOKING"})
    void 주문상태가_조리중이거나_식사중_상태이면_테이블을_비어있음_상태로_변경할_수_없다(final String status) {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        주문을_저장한다(new Order(savedOrderTable.getId(), OrderStatus.valueOf(status), LocalDateTime.now(), new ArrayList<>()));

        final OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_고객수를_변경할_수_있다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest = new OrderTableNumberOfGuestsRequest(4);

        // when
        final OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                orderTableNumberOfGuestsRequest);

        // then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void 테이블의_고객수를_변경할_때_고객수가_음수이면_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());

        final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest = new OrderTableNumberOfGuestsRequest(-1);

        // when
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_고객수를_변경할_때_테이블이_존재하지_않으면_예외가_발생한다() {
        // given
        final long notExistOrderTableId = Long.MAX_VALUE;
        final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest = new OrderTableNumberOfGuestsRequest(4);

        // when
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(notExistOrderTableId, orderTableNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_고객수를_변경할_때_테이블이_비어있으면_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest = new OrderTableNumberOfGuestsRequest(4);

        // when
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
