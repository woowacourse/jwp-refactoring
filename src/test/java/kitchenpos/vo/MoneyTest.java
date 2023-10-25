package kitchenpos.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MoneyTest {
    @Test
    void 정상적으로_생성한다() {
        // given
        BigDecimal price = BigDecimal.ONE;

        // expect
        assertThatNoException().isThrownBy(() -> Money.valueOf(price));
    }

    @ParameterizedTest
    @CsvSource({"0, true", "1, false", "2, false"})
    void 돈이_특정_금액을_초과하는지_확인한다(long value, boolean expected) {
        // given
        Money money = Money.valueOf(BigDecimal.ONE);
        BigDecimal price = BigDecimal.valueOf(value);

        // when
        boolean actual = money.isGreaterThan(Money.valueOf(price));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 돈을_더한다() {
        // given
        Money money = Money.valueOf(BigDecimal.ONE);

        // when
        Money amount = money.add(Money.valueOf(BigDecimal.ONE));

        // then
        assertThat(amount.getValue()).isEqualTo(BigDecimal.valueOf(2L));
    }

    @Test
    void 돈을_곱한다() {
        // given
        Money money = Money.valueOf(BigDecimal.ONE);

        // when
        Money amount = money.multiply(10L);

        // then
        assertThat(amount.getValue()).isEqualTo(BigDecimal.TEN);
    }
}
