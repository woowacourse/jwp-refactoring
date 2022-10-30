package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void quantity가_1보다_작으면_예외를_반환한다(Long value) {
        assertThatThrownBy(() -> new Quantity(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
