package kitchenpos.domain;

import static kitchenpos.domain.exception.OrderTableExceptionType.NUMBER_OF_GUEST_LOWER_THAN_ZERO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("OrderTable의 numberOfGuest가 0이하면 예외처리한다.")
    void validateNumberOfGuest() {
        assertThatThrownBy(() -> new OrderTable(1L, -1, true))
            .isInstanceOf(OrderTableException.class)
            .hasMessage(NUMBER_OF_GUEST_LOWER_THAN_ZERO.getMessage());
    }

}
