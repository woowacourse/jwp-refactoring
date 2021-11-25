package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.exception.InvalidNumberOfGuestsException;
import kitchenpos.table.exception.OrderTableEmptyException;
import kitchenpos.table.exception.OrderTableNotEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable 단위 테스트")
class OrderTableTest {

    private static final Long TABLE_GROUP_ID = 1L;

    @DisplayName("OrderTable을 생성할 때 손님 수가 음수면 예외가 발생한다.")
    @Test
    void numberOfGuestsException() {
        // when, then
        assertThatThrownBy(() -> new OrderTable(-1, true))
            .isExactlyInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @DisplayName("손님 수를 변경할 때")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("OrderTable이 비어있는 상태일 경우 예외가 발생한다.")
        @Test
        void emptyException() {
            // given
            OrderTable orderTable = new OrderTable(5, true);
            NumberOfGuests numberOfGuests = new NumberOfGuests(1);

            // when, then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isExactlyInstanceOf(OrderTableEmptyException.class);
        }

        @DisplayName("손님 수가 음수일 경우 예외가 발생한다.")
        @Test
        void negativeException() {
            // given
            OrderTable orderTable = new OrderTable(5, false);

            // when, then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(new NumberOfGuests(-1)))
                .isExactlyInstanceOf(InvalidNumberOfGuestsException.class);
        }
    }

    @DisplayName("OrderTable의 상태를 변경할 때 TagleGroup과 매핑되어 있는 경우 예외가 발생한다.")
    @Test
    void orderTableMappingTableGroupException() {
        // given
        OrderTable orderTable = new OrderTable(5, false);
        orderTable.groupBy(TABLE_GROUP_ID);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
            .isExactlyInstanceOf(OrderTableNotEmptyException.class);
    }

    @DisplayName("TableGroup과 매핑되면 empty 상태가 변경된다.")
    @Test
    void groupBy() {
        // given
        OrderTable orderTable = new OrderTable(5, true);

        // when
        assertThat(orderTable.isNotEmpty()).isFalse();
        assertThat(orderTable.isGrouped()).isFalse();

        orderTable.groupBy(TABLE_GROUP_ID);

        // then
        assertThat(orderTable.isNotEmpty()).isTrue();
        assertThat(orderTable.isGrouped()).isTrue();
    }

    @DisplayName("TableGroup과 매핑이 해제되면 empty 상태가 변경된다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable = new OrderTable(5, true);
        orderTable.groupBy(TABLE_GROUP_ID);

        // when
        assertThat(orderTable.isNotEmpty()).isTrue();
        assertThat(orderTable.isGrouped()).isTrue();

        orderTable.ungroup();

        // then
        assertThat(orderTable.isNotEmpty()).isFalse();
        assertThat(orderTable.isGrouped()).isFalse();
    }
}
