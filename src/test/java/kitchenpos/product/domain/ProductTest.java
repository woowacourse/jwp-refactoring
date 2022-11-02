package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        assertThatThrownBy(() ->  new Product("hello", new Price(price)))
                .isExactlyInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("제품의 수량을 곱한 총 수량 가격을 구한다.")
    void calculateAmount() {
        // given
        final Product product = new Product("product", new Price(1000L));

        // when
        final Price actual = product.calculateAmount(10);

        // then
        assertThat(actual).isEqualTo(new Price(10000L));
    }
}
