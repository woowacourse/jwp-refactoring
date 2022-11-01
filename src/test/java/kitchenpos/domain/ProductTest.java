package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ProductTest {

    @DisplayName("가격이 음수인 경우 예외가 발생한다.")
    @ValueSource(ints={-1, -2})
    @ParameterizedTest
    void PriceIsNotNegative(int price) {
        assertThatThrownBy(() -> new Product("상품상품", BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0, 양수인 경우 예외가 발생하지 않는다.")
    @ValueSource(ints={0, 1000})
    @ParameterizedTest
    void PriceIsPositive(int price) {
        assertDoesNotThrow(() -> new Product("상품상품", BigDecimal.valueOf(price)));
    }
}
