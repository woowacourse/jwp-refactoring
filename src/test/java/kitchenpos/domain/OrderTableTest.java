package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableTest {

    @DisplayName("OrderTable의 NumberOfGuests가 음수일 때")
    @Test
    void hasMinusNumberOfGuests_whenNumberOfGuestsIsMinus() {
        OrderTable orderTable = new OrderTable(1L, 1L, -1, true);

        assertThat(orderTable.hasMinusNumberOfGuests()).isEqualTo(true);
    }

    @DisplayName("OrderTable의 NumberOfGuests가 0일 때")
    @Test
    void hasMinusNumberOfGuests_whenNumberOfGuestsIsZero() {
        OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

        assertThat(orderTable.hasMinusNumberOfGuests()).isEqualTo(false);
    }

    @DisplayName("OrderTable의 NumberOfGuests가 양수일 때")
    @Test
    void hasMinusNumberOfGuests_whenNumberOfGuestsIsPlus() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

        assertThat(orderTable.hasMinusNumberOfGuests()).isEqualTo(false);
    }

    @DisplayName("OrderTable의 empty가 true일 때")
    @Test
    void isEmpty_whenEmptyIsTrue() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

        assertThat(orderTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("OrderTable의 empty가 false일 때")
    @Test
    void isEmpty_whenEmptyIsFalse() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        assertThat(orderTable.isEmpty()).isEqualTo(false);
    }
}