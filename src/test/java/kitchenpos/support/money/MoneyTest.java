package kitchenpos.support.money;

import static java.util.function.Function.identity;
import static kitchenpos.support.money.Money.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MoneyTest {

    @Test
    void 더한다() {
        // given
        Money money = valueOf(1000L);

        // when
        Money result = money.plus(valueOf(1500));

        // then
        assertThat(result).isEqualTo(valueOf(2500));
    }

    @Test
    void 곱한다() {
        // given
        Money money = valueOf(1000L);

        // when
        Money result = money.times(2);

        // then
        assertThat(result).isEqualTo(valueOf(2000));
    }

    @CsvSource({"999, true", "1000, false", "1001, false"})
    @ParameterizedTest
    void 더_큰_값인지_비교한다(Long value, boolean result) {
        // given
        Money money = valueOf(1000L);

        // expect
        assertThat(money.isGreaterThan(valueOf(value))).isEqualTo(result);
    }

    @CsvSource({"999, false", "1000, false", "1001, true"})
    @ParameterizedTest
    void 더_작은_값인지_비교한다(Long value, boolean result) {
        // given
        Money money = valueOf(1000L);

        // expect
        assertThat(money.isLessThan(valueOf(value))).isEqualTo(result);
    }

    @Test
    void 모든_값을_더한다() {
        // given
        List<Money> moneys = List.of(valueOf(1000), valueOf(1500));

        // when
        Money result = Money.sum(moneys, identity());

        // then
        assertThat(result).isEqualTo(Money.valueOf(2500));
    }
}
