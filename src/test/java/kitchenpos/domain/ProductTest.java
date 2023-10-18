package kitchenpos.domain;

import static java.math.BigDecimal.valueOf;
import static kitchenpos.domain.exception.ProductExceptionType.PRICE_IS_LOWER_THAN_ZERO;
import static kitchenpos.domain.exception.ProductExceptionType.PRICE_IS_NULL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("price가 0보다 작은 경우 예외처리한다.")
    void validatePriceLowerThan0() {
        final BigDecimal invalidatePrice = valueOf(-1);
        assertThatThrownBy(() -> new Product("test", invalidatePrice))
            .isInstanceOf(ProductException.class)
            .hasMessage(PRICE_IS_LOWER_THAN_ZERO.getMessage());
    }

    @Test
    @DisplayName("price가 0보다 작은 경우 예외처리한다.")
    void validatePriceIsNull() {
        assertThatThrownBy(() -> new Product("test", null))
            .isInstanceOf(ProductException.class)
            .hasMessage(PRICE_IS_NULL.getMessage());
    }
}
