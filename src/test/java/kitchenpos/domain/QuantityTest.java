package kitchenpos.domain;

import kitchenpos.exception.domain.quantity.InvalideQuantityValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuantityTest {
    @Test
    @DisplayName("수량은 0보다 작을 수 없다.")
    void validation() {
        assertThatThrownBy(() -> Quantity.from(-1L))
                .isInstanceOf(InvalideQuantityValueException.class);
    }
}
