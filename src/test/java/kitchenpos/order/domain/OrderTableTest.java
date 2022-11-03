package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("손님수를 변경한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void updateNumberOfGuests(final int number) {
        final var numberOfGuests = new GuestCount(number);

        final var orderTable = makeNonEmptyOrderTable(10);
        orderTable.updateNumberOfGuests(numberOfGuests);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("공석테이블의 손님수를 변경한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void updateNumberOfGuestsWithEmptyTable(final int number) {
        final var numberOfGuests = new GuestCount(number);

        final var orderTable = makeEmptyOrderTable();
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있습니다.");
    }

    @DisplayName("공석여부를 변경한다")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmptyStatusTo(final boolean empty) {
        final var orderTable = makeEmptyOrderTable();
        orderTable.changeEmptyStatusTo(empty);

        assertThat(orderTable.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("공석여부를 확인한다")
    @Test
    void isEmpty() {
        final var orderTable = makeEmptyOrderTable();
        assertThat(orderTable.isEmpty()).isTrue();
        assertThat(orderTable.isNotEmpty()).isFalse();
    }

    private OrderTable makeEmptyOrderTable() {
        return new OrderTable(new GuestCount(0), true);
    }

    private OrderTable makeNonEmptyOrderTable(final int numberOfGuests) {
        return new OrderTable(new GuestCount(numberOfGuests), false);
    }
}
