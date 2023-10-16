package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new Product("테스트용 상품명", new BigDecimal("10000")))
                .doesNotThrowAnyException();
    }

    @DisplayName("[EXCEPTION] 가격이 null 일 경우 예외가 발생한다.")
    @Test
    void throwException_when_price_isNull() {
        // expect
        assertThatThrownBy(() -> new Product("테스트용 상품명", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[EXCEPTION] 가격이 음수일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1", "-10", "-100", "-1000000"})
    void throwException_when_price_isNegative(final String value) {
        // expect
        assertThatThrownBy(() -> new Product("테스트용 상품명", new BigDecimal(value)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
