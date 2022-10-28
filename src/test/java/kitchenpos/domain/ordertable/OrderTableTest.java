package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void 상태_변경_시_tableGroup이_존재하면_예외가_발생한다(final boolean empty) {
        final OrderTable orderTable = new OrderTable(1L, 5, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(empty))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OrderTable이 이미 TableGroup에 속해있습니다.");
    }

    @Test
    void 손님_변경시_손님_수가_음수이면_예외가_발생한다() {
        final OrderTable orderTable = new OrderTable(1L, 5, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 음수일 수 없습니다.");
    }

    @Test
    void tableGroup_등록_시_비어있지_않으면_예외가_발생한다() {
        final OrderTable orderTable = new OrderTable(null, 5, false);

        assertThatThrownBy(() -> orderTable.addTableGroup(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상태가 empty가 아니거나 tableGroup이 이미 존재합니다.");
    }

    @Test
    void tableGroup_등록_시_이미_tableGroup이_존재하면_예외가_발생한다() {
        final OrderTable orderTable = new OrderTable(1L, 5, true);

        assertThatThrownBy(() -> orderTable.addTableGroup(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상태가 empty가 아니거나 tableGroup이 이미 존재합니다.");
    }

    @Test
    void tableGroup에_등록되면_비어있지_않은_상태로_변경한다() {
        final OrderTable orderTable = new OrderTable(null, 5, true);

        orderTable.addTableGroup(1L);

        assertThat(orderTable)
                .extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                .containsExactly(1L, false);
    }

    @Test
    void tableGroup에서_제외한다() {
        final OrderTable orderTable = new OrderTable(1L, 5, false);

        orderTable.deleteTableGroup();

        assertThat(orderTable)
                .extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                .containsExactly(null, false);
    }
}
