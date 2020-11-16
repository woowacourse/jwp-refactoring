package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {
    @DisplayName("잘못된 가격이 입력되었을 시 예외 처리한다.")
    @ParameterizedTest
    @NullSource
    void ofWithNull(BigDecimal price) {
        assertThatThrownBy(() -> Product.of("test", price))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("0 미만 가격이 입력되었을 시 예외 처리한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -1_000L})
    void ofWithNegative(Long price) {
        assertThatThrownBy(() -> Product.of("test", BigDecimal.valueOf(price)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}