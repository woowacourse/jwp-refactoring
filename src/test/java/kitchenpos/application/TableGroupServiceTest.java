package kitchenpos.application;

import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_EMPTY_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exceptions.NotEnoughSizeOfOrderTableException;
import kitchenpos.exceptions.OrderNotCompletionException;
import kitchenpos.exceptions.OrderTableAlreadyHasTableGroupException;
import kitchenpos.exceptions.OrderTableNotEmptyException;
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
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(new OrderTableIdRequest(firstSavedOrderTable.getId()),
                        new OrderTableIdRequest(secondSavedOrderTable.getId())));

        // when
        final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(tableGroupResponse.getId()).isEqualTo(1L);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_그룹이_2개이상이_아니면_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(new OrderTableIdRequest(savedOrderTable.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotEnoughSizeOfOrderTableException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_테이블이_존재하지_않으면_예외를_발생한다() {
        // given
        final Long notExistOrderTableId = Long.MAX_VALUE;
        final OrderTable secondSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(new OrderTableIdRequest(notExistOrderTableId),
                        new OrderTableIdRequest(secondSavedOrderTable.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotEnoughSizeOfOrderTableException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_테이블이_비어있지_않다면_예외를_발생한다() {
        // given
        final OrderTable fullSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        final OrderTable emptySavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(new OrderTableIdRequest(fullSavedOrderTable.getId()),
                        new OrderTableIdRequest(emptySavedOrderTable.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(OrderTableNotEmptyException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_테이블중_이미_테이블_그룹이_있다면_예외를_발생한다() {
        // given
        final OrderTable orderTable1_hasTableGroup = ORDER_TABLE_EMPTY_1.생성();
        final OrderTable orderTable2_hasTableGroup = ORDER_TABLE_EMPTY_1.생성();
        테이블그룹을_저장한다(TABLE_GROUP_NOW.생성(List.of(orderTable1_hasTableGroup, orderTable2_hasTableGroup)));

        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(new OrderTableIdRequest(orderTable1_hasTableGroup.getId()),
                        new OrderTableIdRequest(savedOrderTable.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(OrderTableAlreadyHasTableGroupException.class);
    }

    @Test
    void 테이블_그룹을_해제할_수_있다() {
        // given
        final OrderTable orderTable1 = ORDER_TABLE_EMPTY_1.생성();
        final OrderTable orderTable2 = ORDER_TABLE_EMPTY_1.생성();
        final TableGroup tableGroup = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성(List.of(orderTable1, orderTable2)));
        주문항목과_함께_주문을_저장한다(orderTable1.getId(), OrderStatus.COMPLETION);
        주문항목과_함께_주문을_저장한다(orderTable2.getId(), OrderStatus.COMPLETION);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTableResponse> orderTableResponses = tableService.list();
        assertAll(
                () -> assertThat(orderTableResponses)
                        .extracting("tableGroupId")
                        .containsExactly(null, null),
                () -> assertThat(orderTableResponses)
                        .extracting("empty")
                        .containsExactly(false, false)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 테이블_그룹을_해제할_때_주문테이블의_주문상태가_제조중이거나_식사중이면_예외를_발생한다(final String status) {
        // given
        final OrderTable orderTable1 = ORDER_TABLE_EMPTY_1.생성();
        final OrderTable orderTable2 = ORDER_TABLE_EMPTY_1.생성();
        final TableGroup tableGroup = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성(List.of(orderTable1, orderTable2)));
        주문항목과_함께_주문을_저장한다(orderTable1.getId(), OrderStatus.valueOf(status));
        주문항목과_함께_주문을_저장한다(orderTable2.getId(), OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(OrderNotCompletionException.class);
    }
}
