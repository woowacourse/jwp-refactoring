package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void 가격이_0보다_작은_경우_예외를_던진다(final int price) {
        // when & then
        assertThatThrownBy(() -> Price.valueOf(price))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.PRICE_MIN_VALUE_ERROR);
    }

    @Test
    void 가격은_0일_수_있다() {
        // given
        final var value = 0;

        // when
        final var actual = Price.valueOf(value);

        // then
        assertThat(actual.getValue()).isEqualByComparingTo(BigDecimal.valueOf(value));
    }

    @Test
    void 가격을_수량만큼_곱한다() {
        // given
        final var price = Price.valueOf(10_000);
        final var quantity = new Quantity(5);

        // when
        final var multiplied = price.multiply(quantity);

        // then
        assertThat(multiplied).isEqualTo(Price.valueOf(50_000));
    }

    @Test
    void 가격끼리_더한다() {
        // given
        final var priceA = Price.valueOf(10_000);
        final var priceB = Price.valueOf(20_000);

        // when
        final var summed = priceA.sum(priceB);

        // then
        assertThat(summed).isEqualTo(Price.valueOf(30_000));
    }

    @ParameterizedTest
    @CsvSource({
            "10000, 20000, false",
            "10000, 10000, false",
            "20000, 10000, true"
    })
    void 더_큰지_비교한다(final int valueA, final int valueB, final boolean expected) {
        // given
        final var priceA = Price.valueOf(valueA);
        final var priceB = Price.valueOf(valueB);

        // when
        final var greaterThan = priceA.isGreaterThan(priceB);

        // then
        assertThat(greaterThan).isEqualTo(expected);
    }
}
