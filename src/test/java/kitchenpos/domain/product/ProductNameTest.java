package kitchenpos.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductNameTest {
    @DisplayName("상품 이름이 비어 있거나 공백이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void validateMenuNameBlank(String value) {
        assertThatThrownBy(() -> new ProductName(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 이름이 null이면 예외가 발생한다.")
    void validateMenuNameNull() {
        assertThatThrownBy(() -> new ProductName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
