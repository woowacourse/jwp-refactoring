package kitchenpos.application;

import static kitchenpos.exception.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyCommand;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsCommand;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationTest {

    @Test
    void 주문_테이블을_저장한다() {
        // given
        OrderTable orderTable1 = new OrderTable(null, null, 0, false);

        // when
        OrderTable result = tableService.create(orderTable1);

        // then
        assertThat(result.id()).isPositive();
    }

    @Test
    void 주문_테이블들을_조회한다() {
        // given
        OrderTable orderTable1 = new OrderTable(null, null, 0, false);
        OrderTable orderTable2 = new OrderTable(null, null, 0, false);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).id()).isEqualTo(savedOrderTable1.id()),
                () -> assertThat(result.get(1).id()).isEqualTo(savedOrderTable2.id())
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
            OrderTable orderTable1 = new OrderTable(null, null, 0, true);
            OrderTable orderTable2 = new OrderTable(null, null, 0, true);

            TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
            TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

            orderTable1.setTableGroup(savedTableGroup);
            Long id = orderTableRepository.save(orderTable1).id();

            ChangeOrderTableEmptyCommand command = new ChangeOrderTableEmptyCommand(id, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 조리중이거나_식사중인_테이블을_변경하면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, null, 0, false);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            주문(savedOrderTable1, OrderStatus.COOKING, 맛있는_메뉴());
            ChangeOrderTableEmptyCommand command = new ChangeOrderTableEmptyCommand(savedOrderTable1.id(), true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_상태를_변경한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, null, 0, true);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            ChangeOrderTableEmptyCommand command = new ChangeOrderTableEmptyCommand(savedOrderTable1.id(), false);

            // when
            OrderTable result = tableService.changeEmpty(command);

            // then
            assertThat(result.empty()).isFalse();
        }
    }

    @Nested
    class 손님_숫자_변경 {

        @Test
        void 손님_숫자가_0보다_작으면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, null, 0, false);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            ChangeOrderTableNumberOfGuestsCommand command =
                    new ChangeOrderTableNumberOfGuestsCommand(savedOrderTable1.id(), -1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블을_변경하면_예외가_발생한다() {
            // given
            ChangeOrderTableNumberOfGuestsCommand command =
                    new ChangeOrderTableNumberOfGuestsCommand(1L, 2);

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
            OrderTable orderTable1 = new OrderTable(null, null, 0, true);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            ChangeOrderTableNumberOfGuestsCommand command =
                    new ChangeOrderTableNumberOfGuestsCommand(savedOrderTable1.id(), 2);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 손님_숫자를_변경한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null, null, 0, false);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            ChangeOrderTableNumberOfGuestsCommand command =
                    new ChangeOrderTableNumberOfGuestsCommand(savedOrderTable1.id(), 2);

            // when
            OrderTable result = tableService.changeNumberOfGuests(command);

            // then
            assertThat(result.numberOfGuests()).isEqualTo(2);
        }
    }
}
