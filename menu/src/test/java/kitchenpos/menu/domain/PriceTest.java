package kitchenpos.menu.domain;

import static kitchenpos.menu.exception.PriceExceptionType.PRICE_CAN_NOT_NEGATIVE;
import static kitchenpos.menu.exception.PriceExceptionType.PRICE_CAN_NOT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @Test
    void 가격이_null_이면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new Price(null)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NULL);
    }

    @Test
    void 가격이_0보다_작으면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new Price(BigDecimal.valueOf(-1))
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NEGATIVE);
    }

    @Test
    void 가격에_곱을하면_곱한값을_가진_가격을_반환한다() {
        // given
        Price price = new Price(BigDecimal.valueOf(3));

        // when
        Price result = price.multiply(3);

        // then
        assertThat(result).isEqualTo(new Price(BigDecimal.valueOf(9)));
    }

    @Test
    void 가격에_합을하면_합한값을_가진_가격을_반환한다() {
        // given
        Price price = new Price(BigDecimal.valueOf(3));

        // when
        Price result = price.add(new Price(BigDecimal.valueOf(3)));

        // then
        assertThat(result).isEqualTo(new Price(BigDecimal.valueOf(6)));
    }

    @Test
    void 가격이_다른가격보다_크면_true를_반환한다() {
        // given
        Price price1 = new Price(BigDecimal.valueOf(3));
        Price price2 = new Price(BigDecimal.valueOf(1));

        // when
        boolean result = price1.isGreaterThan(price2);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void 가격이_다른가격보다_작거나_같으면_false를_반환한다(int value) {
        // given
        Price price1 = new Price(BigDecimal.valueOf(1));
        Price price2 = new Price(BigDecimal.valueOf(value));

        // when
        boolean result = price1.isGreaterThan(price2);

        // then
        assertThat(result).isFalse();
    }
}
