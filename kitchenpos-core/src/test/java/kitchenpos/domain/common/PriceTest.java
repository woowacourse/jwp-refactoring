package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.badrequest.NegativePriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @Test
    void 가격은_음수일_수_없다() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1))).isInstanceOf(NegativePriceException.class);
    }

    @Test
    void 가격_동등_비교가_가능하다() {
        Price price1 = new Price(BigDecimal.ONE);
        Price price2 = new Price(BigDecimal.ZERO);

        boolean actual = price1.equals(price2);

        assertThat(actual).isFalse();
    }

    @Test
    void 가격의_합연산을_할_수_있다() {
        Price price1 = new Price(BigDecimal.ONE);
        Price price2 = new Price(BigDecimal.ONE);

        Price sum = price1.add(price2);

        assertThat(sum).isEqualTo(new Price(BigDecimal.valueOf(2)));
    }

    @Test
    void 가격의_곱연산을_할_수_있다() {
        Price price = new Price(BigDecimal.valueOf(2));
        long times = 5;

        Price multiple = price.multiply(times);

        assertThat(multiple).isEqualTo(new Price(BigDecimal.TEN));
    }

    @ParameterizedTest
    @CsvSource(value = {"0,true", "1,false"})
    void 가격이_더_비싼지_판정할_수_있다(final long value, final boolean expected) {
        Price price1 = new Price(BigDecimal.ONE);
        Price price2 = new Price(BigDecimal.valueOf(value));

        boolean actual = price1.isExpensiveThan(price2);

        assertThat(actual).isEqualTo(expected);
    }
}
