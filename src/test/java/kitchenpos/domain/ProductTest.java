package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("상품 가격이 정상적인 경우 생성할 수 있다.")
    @Test
    void createProduct() {
        assertDoesNotThrow(() -> new Product("짱구", BigDecimal.ZERO));
    }

    @DisplayName("상품 가격이 없는 경우 생성할 수 없다.")
    @Test
    void createWithNullPrice() {
        assertThatThrownBy(() -> new Product("짱구", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 0원보다 적은 경우 생성할 수 없다.")
    @Test
    void createWithPriceLessThanZero() {
        assertThatThrownBy(() -> new Product("짱구", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
