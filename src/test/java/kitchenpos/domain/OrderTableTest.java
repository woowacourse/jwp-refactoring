package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("numberOfGuests가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void createProductByPriceNegative(final int numberOfGuests) {
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}