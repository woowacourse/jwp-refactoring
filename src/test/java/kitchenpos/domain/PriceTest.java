package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @ParameterizedTest
    @CsvSource(value = {"900, true", "1000, false", "1100, false"})
    void 가격을_비교한다(final int comparedPrice, final boolean expected) {
        // given
        final Price price = new Price(new BigDecimal(1_000));
        final Price another = new Price(new BigDecimal(comparedPrice));

        // when
        final boolean actual = price.isGreaterThan(another);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 가격을_정수로_곱할_수_있다() {
        // given
        final Price price = new Price(new BigDecimal(1_000));
        final Price expected = new Price(new BigDecimal(2_000));

        // when
        final Price actual = price.multiply(2);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 가격끼리_더할_수_있다() {
        // given
        final Price price = new Price(new BigDecimal(1_000));
        final Price another = new Price(new BigDecimal(2_000));
        final Price expected = new Price(new BigDecimal(3_000));
        // when
        final Price actual = price.add(another);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
