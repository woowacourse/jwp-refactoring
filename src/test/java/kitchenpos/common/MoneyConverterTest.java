package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
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
        BigDecimal result = moneyConverter.convertToDatabaseColumn(money);

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(1000L));
    }

    @Test
    void Money로_변환한다() {
        // given
        BigDecimal amount = BigDecimal.valueOf(1000L);

        // when
        Money result = moneyConverter.convertToEntityAttribute(amount);

        // then
        assertThat(result).isEqualTo(Money.valueOf(1000L));
    }
}
