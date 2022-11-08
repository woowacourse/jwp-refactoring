package kitchenpos.core.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @Test
    @DisplayName("상품의 가격이 0 일 경우 상품이 생성된다.")
    void createWthZeroPrice() {
        assertDoesNotThrow(() -> new Product(1L, "후라이드", BigDecimal.valueOf(0)));
    }

    @Test
    @DisplayName("상품의 가격이 null 일 경우 예외가 발생한다.")
    void createWithNullPrice() {
        assertThatThrownBy(() -> new Product(1L, "후라이드", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "상품의 가격이 음수({0})일 경우 상품을 생성하면 예외가 발생한다.")
    @ValueSource(ints = {-100, -1})
    void createWithNegativePrice(final int price) {
        assertThatThrownBy(() -> new Product(1L, "후라이드", BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
