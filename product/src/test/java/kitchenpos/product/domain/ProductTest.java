package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @DisplayName("상품 이름은 null일 수 없다.")
    @Test
    void createProductFailTest_ByNameIsNull() {
        assertThatThrownBy(() -> Product.create(null, BigDecimal.TEN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름은 1글자 이상, 255자 이하여야 합니다.");
    }

    @ParameterizedTest(name = "상품 이름은 1글자 이상, 255자 이하여야 한다.")
    @ValueSource(ints = {0, 256})
    void createProductFailTest_ByNameLengthIsNotInRange(int count) {
        String name = "a".repeat(count);

        assertThatThrownBy(() -> Product.create(name, BigDecimal.TEN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름은 1글자 이상, 255자 이하여야 합니다.");
    }

    @DisplayName("상품 가격은 0 미만일 수 없다.")
    @Test
    void createProductFailTest_ByPriceLessThanZero() {
        assertThatThrownBy(() -> Product.create("Test", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 금액은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProductSuccessTest() {
        assertDoesNotThrow(() -> Product.create("Test", BigDecimal.TEN));
    }

}
