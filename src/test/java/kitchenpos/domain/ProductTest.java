package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.ProductNegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class ProductTest {

    @ParameterizedTest
    @NullSource
    @CsvSource({"-1"})
    @DisplayName("제품 가격이 Null이거나 음수이면 안된다.")
    void create_exceptionPriceIsNullOrNegative(final BigDecimal price) {
        // given, when, then
        assertThatThrownBy(() ->  new Product("hello", price))
                .isExactlyInstanceOf(ProductNegativePriceException.class);
    }
}
