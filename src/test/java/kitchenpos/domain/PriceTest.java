package kitchenpos.domain;

import kitchenpos.exception.domain.price.InvalidePriceValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {
    @Test
    @DisplayName("가격은 null일 수 없다.")
    void validateNull() {
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(InvalidePriceValueException.class);
    }

    @Test
    @DisplayName("가격은 0보다 작을 수 없다.")
    void validateNegativeNumber() {
        assertThatThrownBy(() -> Price.from(-1L))
                .isInstanceOf(InvalidePriceValueException.class);
    }
}
