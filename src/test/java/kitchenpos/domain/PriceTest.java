package kitchenpos.domain;

import kitchenpos.common.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    void Price는_입력으로_null이_들어오면_생성에_실패한다() {
        // given
        final BigDecimal value = null;

        // when, then
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Price는_입력으로_0미만의_수가_들어오면_생성에_실패한다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(-1000);

        // when, then
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Price끼리_더한_Price를_반환한다() {
        // given
        final Price price = new Price(BigDecimal.valueOf(1000));
        final Price otherPrice = new Price(BigDecimal.valueOf(100));

        // when
        final Price result = price.add(otherPrice);

        // then
        assertThat(result.getValue()).isEqualTo(BigDecimal.valueOf(1100));
    }

    @Test
    void Price에_정수를_곱한_Price를_반환한다() {
        // given
        final Price price = new Price(BigDecimal.valueOf(1000));
        final long number = 10;

        // when
        final Price result = price.multiply(number);

        // then
        assertThat(result.getValue()).isEqualTo(BigDecimal.valueOf(10000));
    }

    @Test
    void isMoreThan에서_현재_Price보다_작은_Price를_입력으로_받으면_true를_반환한다() {
        // given
        final Price price = new Price(BigDecimal.valueOf(2000));
        final Price otherPrice = new Price(BigDecimal.valueOf(1000));

        // when
        final boolean result = price.isMoreThan(otherPrice);

        // then
        assertThat(result).isTrue();
    }
}
