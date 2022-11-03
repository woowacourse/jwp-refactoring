package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class PriceTest {

    @Test
    void Price_객체를_생성한다() {
        // given
        final BigDecimal price = BigDecimal.valueOf(1);

        // when
        final Price actual = new Price(price);

        // then
        assertThat(actual.getValue()).isEqualTo(price);

    }

    @ParameterizedTest
    @NullSource
    @CsvSource("-1")
    void 잘못된_값으로는_Price_객체를_생성할_수_없다(BigDecimal price) {
        // when
        assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격을_곱할_수_있다() {
        // given
        final int value = 1000;
        final Price price = new Price(BigDecimal.valueOf(value));

        // when
        final BigDecimal actual = price.multiply(3);

        // then
        assertThat(actual).isEqualTo(BigDecimal.valueOf(value * 3));
    }
}
