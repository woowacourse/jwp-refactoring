package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyCommand;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationTest {

    @Test
    void 주문_테이블을_저장한다() {
        // given
        OrderTable orderTable1 = new OrderTable(null);

        // when
        OrderTable result = tableService.create(orderTable1);

        // then
        assertThat(result.id()).isPositive();
    }

    @Test
    void 주문_테이블들을_조회한다() {
        // given
        OrderTable orderTable1 = new OrderTable(null);
        OrderTable orderTable2 = new OrderTable(null);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

        // when
        List<OrderTable> result = tableService.list();

        // then
        Assertions.assertAll(
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

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정된_단체가_있는_테이블을_변경하면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(0, true);
            OrderTable orderTable2 = new OrderTable(0, true);

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
            OrderTable orderTable1 = new OrderTable(0, false);
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
            OrderTable orderTable1 = new OrderTable(0, true);
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
            OrderTable orderTable1 = new OrderTable(null);
            OrderTable orderTable2 = new OrderTable(-1, true);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable1.id(), orderTable2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블을_변경하면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(2, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블을_변경하면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(0, true);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            OrderTable orderTable2 = new OrderTable(2, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable1.id(), orderTable2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 손님_숫자를_변경한다() {
            // given
            OrderTable orderTable1 = new OrderTable(null);
            OrderTable orderTable2 = new OrderTable(2, false);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

            // when
            OrderTable result = tableService.changeNumberOfGuests(savedOrderTable1.id(), orderTable2);

            // then
            assertThat(result.numberOfGuests()).isEqualTo(2);
        }
    }
}
