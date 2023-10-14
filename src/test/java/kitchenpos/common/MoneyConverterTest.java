package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.vo.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MoneyConverterTest {

    private final MoneyConverter moneyConverter = new MoneyConverter();

    @Test
    void Long으로_변환한다() {
        // given
        Money money = Money.valueOf(1000L);

        // when
        Long result = moneyConverter.convertToDatabaseColumn(money);

        // then
        assertThat(result).isEqualTo(1000L);
    }

    @Test
    void Money로_변환한다() {
        // given
        long amount = 1000L;

        // when
        Money result = moneyConverter.convertToEntityAttribute(amount);

        // then
        assertThat(result).isEqualTo(Money.valueOf(1000L));
    }
}
