package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void changeEmpty() {
        // given
        boolean empty = true;
        OrderTable orderTable = new OrderTable(2, false);

        // when
        orderTable.changeEmpty(empty);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeEmptyWithTableGroupId() {
        // given
        boolean empty = true;
        OrderTable orderTable = new OrderTable(1L, 2, false);
        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        int changeNumberOfGuests = 3;

        // when
        orderTable.changeNumberOfGuests(changeNumberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changeNumberOfGuests);
    }

    @Test
    void changeNumberOfGuestsWithEmpty() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        int changeNumberOfGuests = 3;

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changeNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        int changeNumberOfGuests = -1;

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changeNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
