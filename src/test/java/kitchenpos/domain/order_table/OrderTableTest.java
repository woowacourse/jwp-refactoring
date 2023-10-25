package kitchenpos.domain.order_table;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class OrderTableTest {

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-100"})
    @DisplayName("테이블의 손님의 수가 0 미만일 경우 IllegalArgumentException이 발생한다.")
    void changeNumberOfGuestsWithNegativeNumberOfGuests(final int invalidNumberOfGuests) {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> new OrderTable(null, invalidNumberOfGuests, true));
    }
}
