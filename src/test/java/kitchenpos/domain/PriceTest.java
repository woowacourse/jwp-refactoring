package kitchenpos.domain;

import static kitchenpos.exception.PriceExceptionType.PRICE_IS_NEGATIVE_EXCEPTION;
import static kitchenpos.exception.PriceExceptionType.PRICE_IS_NULL_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.PriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @Test
    void 가격은_null일_수_없다() {
        // when
        BaseExceptionType exceptionType = assertThrows(PriceException.class, () ->
                new Price(null)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_IS_NULL_EXCEPTION);
    }

    @Test
    void 가격은_음수일_수_없다() {
        // when
        BaseExceptionType exceptionType = assertThrows(PriceException.class, () ->
                new Price(-1000)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_IS_NEGATIVE_EXCEPTION);
    }

    @ParameterizedTest
    @CsvSource(value = {"500:true", "1000:false", "1500:false"}, delimiter = ':')
    void 다른_Price와_대소비교한다(int otherValue, boolean expected) {
        // given
        Price price = new Price(1000);
        Price other = new Price(otherValue);

        // when
        boolean result = price.isBiggerThan(other);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void 다른_가격과_합한다() {
        // given
        Price price = new Price(1000);
        Price other = new Price(1000);

        // when
        Price result = price.add(other);

        // then
        assertThat(result).isEqualTo(new Price(2000));
    }

    @Test
    void 수량을_곱해_총합을_계산한다() {
        // given
        Price price = new Price(1000);
        long quantity = 10;

        // when
        Price amount = price.calculateAmount(quantity);

        // then
        assertThat(amount).isEqualTo(new Price(1000 * 10));
    }

    @Test
    void 값이_같으면_동등한_객체이다() {
        // given
        Price price = new Price(1000);
        Price other = new Price(1000);

        // then
        assertThat(price).isEqualTo(other);
    }
}
