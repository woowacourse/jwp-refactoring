package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("가격을 생성할 때, 값이 없으면 예외가 발생한다.")
    @Test
    void constructor_throwsException_ifNoPrice() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Price(null));
    }

    @ValueSource(ints = {-1, -500, -1000})
    @ParameterizedTest(name = "가격을 생성할 때, 값이 0보다 작으면 예외가 발생한다. ({0})")
    void constructor_throwsException_ifPriceUnder0(final int value) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Price.from(value));
    }
}
