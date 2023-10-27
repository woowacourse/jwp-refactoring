package kitchenpos.application.table;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.exception.order.OrderExceptionType.ORDER_STATUS_IS_COOKING_OR_MEAL;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE;
import static kitchenpos.exception.table.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.application.IntegrationTest;
import kitchenpos.application.table.dto.ChangeOrderTableEmptyCommand;
import kitchenpos.application.table.dto.ChangeOrderTableEmptyResponse;
import kitchenpos.application.table.dto.ChangeOrderTableNumberOfGuestsCommand;
import kitchenpos.application.table.dto.ChangeOrderTableNumberOfGuestsResponse;
import kitchenpos.application.table.dto.CreateOrderTableCommand;
import kitchenpos.application.table.dto.CreateOrderTableResponse;
import kitchenpos.application.table.dto.SearchOrderTableResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationTest {

    @Test
    void 주문_테이블을_저장한다() {
        // given
        CreateOrderTableCommand command = new CreateOrderTableCommand(0, false);

        // when
        CreateOrderTableResponse result = tableService.create(command);

        // then
        assertThat(result.id()).isPositive();
    }

    @Test
    void 주문_테이블들을_조회한다() {
        // given
        OrderTable orderTable1 = 주문테이블저장(주문테이블(0, false));
        OrderTable orderTable2 = 주문테이블저장(주문테이블(0, false));

        // when
        List<SearchOrderTableResponse> result = tableService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).id()).isEqualTo(orderTable1.id()),
                () -> assertThat(result.get(1).id()).isEqualTo(orderTable2.id())
        );
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 존재하지_않는_주문_테이블을_변경하면_예외가_발생한다() {
            // given
            ChangeOrderTableEmptyCommand command = new ChangeOrderTableEmptyCommand(1L, false);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    tableService.changeEmpty(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_TABLE_NOT_FOUND);
        }

        @Test
        void 지정된_단체가_있는_테이블을_변경하면_예외가_발생한다() {
            // given
            OrderTable 주문테이블 = 주문테이블(0, true);
            테이블그룹저장(테이블그룹(주문테이블, 주문테이블(0, true)));
            주문테이블저장(주문테이블);
            ChangeOrderTableEmptyCommand command = new ChangeOrderTableEmptyCommand(주문테이블.id(), true);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    tableService.changeEmpty(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE);
        }

        @Test
        void 조리중이거나_식사중인_테이블을_변경하면_예외가_발생한다() {
            // given
            OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, false));
            주문저장(주문(주문테이블, COOKING, 주문항목(맛있는_메뉴_저장(), 0)));
            ChangeOrderTableEmptyCommand command = new ChangeOrderTableEmptyCommand(주문테이블.id(), true);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    tableService.changeEmpty(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_STATUS_IS_COOKING_OR_MEAL);
        }

        @Test
        void 주문_테이블_상태를_변경한다() {
            // given
            OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, true));
            ChangeOrderTableEmptyCommand command = new ChangeOrderTableEmptyCommand(주문테이블.id(), false);

            // when
            ChangeOrderTableEmptyResponse result = tableService.changeEmpty(command);

            // then
            assertThat(result.empty()).isFalse();
        }
    }

    @Nested
    class 손님_숫자_변경 {

        @Test
        void 손님_숫자가_0보다_작으면_예외가_발생한다() {
            // given
            OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, false));
            ChangeOrderTableNumberOfGuestsCommand command = new ChangeOrderTableNumberOfGuestsCommand(주문테이블.id(), -1);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    tableService.changeNumberOfGuests(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE);
        }

        @Test
        void 존재하지_않는_테이블을_변경하면_예외가_발생한다() {
            // given
            Long noExistOrderTableId = -1L;
            ChangeOrderTableNumberOfGuestsCommand command =
                    new ChangeOrderTableNumberOfGuestsCommand(noExistOrderTableId, 2);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    tableService.changeNumberOfGuests(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_TABLE_NOT_FOUND);
        }

        @Test
        void 비어있는_테이블을_변경하면_예외가_발생한다() {
            // given
            OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, true));
            ChangeOrderTableNumberOfGuestsCommand command = new ChangeOrderTableNumberOfGuestsCommand(주문테이블.id(), 2);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    tableService.changeNumberOfGuests(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE);
        }

        @Test
        void 손님_숫자를_변경한다() {
            // given
            OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, false));
            ChangeOrderTableNumberOfGuestsCommand command = new ChangeOrderTableNumberOfGuestsCommand(주문테이블.id(), 2);

            // when
            ChangeOrderTableNumberOfGuestsResponse result = tableService.changeNumberOfGuests(command);

            // then
            assertThat(result.numberOfGuests()).isEqualTo(2);
        }
    }
}
