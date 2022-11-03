package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("상품의 가격은 0원 미만이면 예외를 던진다.")
    void price_underZero_throwException() {
        // when & then
        assertThatThrownBy(() -> new Product("test", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격은 null 이면 예외를 던진다.")
    void price_null_throwException() {
        // when & then
        assertThatThrownBy(() -> new Product("test", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
