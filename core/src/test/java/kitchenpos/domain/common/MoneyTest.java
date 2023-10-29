package kitchenpos.domain.common;

import common.domain.Money;
import kitchenpos.domain.DomainTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest extends DomainTest {
    @Test
    void equals() {
        // given
        final Money money = Money.valueOf(1000L);

        // when & then
        assertThat(money).isEqualTo(Money.valueOf(1000L));
    }

    @ParameterizedTest
    @CsvSource(value = {"10,10,20", "10,20,30", "10,30,40"})
    void add_money_with_another_money(final long firstAmount, final long secondAmount, final long expectedAmount) {
        // given
        final Money firstMoney = Money.valueOf(firstAmount);
        final Money secondMoney = Money.valueOf(secondAmount);

        // when
        final Money actual = firstMoney.add(secondMoney);

        // then
        assertThat(actual).isEqualTo(Money.valueOf(expectedAmount));
    }

    @ParameterizedTest
    @CsvSource(value = {"10,10,100", "10,20,200", "10,30,300"})
    void multiply_money_by_multiplier(final long firstAmount, final long secondAmount, final long expectedAmount) {
        // given
        final Money firstMoney = Money.valueOf(firstAmount);

        // when
        final Money actual = firstMoney.multiply(secondAmount);

        // then
        assertThat(actual).isEqualTo(Money.valueOf(expectedAmount));
    }


    @Test
    void throw_when_is_negative() {
        // given
        final long negativeAmount = -1L;

        // when & then
        assertThatThrownBy(
                () -> Money.valueOf(negativeAmount)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Money.AMOUNT_CANNOT_BE_BELOW_ZERO_ERROR_MESSAGE);
    }

    @Test
    void throw_when_have_more_than_two_decimal_places() {
        // given
        final BigDecimal amountWithMoreThanTwoDecimalPlaces = BigDecimal.valueOf(1.111);

        // when & then
        assertThatThrownBy(
                () -> Money.valueOf(amountWithMoreThanTwoDecimalPlaces)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Money.AMOUNT_IS_NOT_IN_SCALE_TWO_ERROR_MESSAGE);
    }

    @Test
    void throw_when_exceed_maximum_amount() {
        // given
        final BigDecimal amountExceedingMaximumAmount = BigDecimal.valueOf(92233720368547758.09);

        // when & then
        assertThatThrownBy(
                () -> Money.valueOf(amountExceedingMaximumAmount)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Money.AMOUNT_EXCEEDS_OEVR_MAX_AMOUNT_ERROR_MESSAGE);
    }
}