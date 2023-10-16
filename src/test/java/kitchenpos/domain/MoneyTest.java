package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.exception.PriceIsNegativeException;
import kitchenpos.exception.PriceIsNotProvidedException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MoneyTest {

    @Test
    void 가격이_0원_미만이라면_예외를_던진다() {
        // given, when, then
        assertThatThrownBy(() -> new Money(BigDecimal.valueOf(-1L)))
                .isInstanceOf(PriceIsNegativeException.class);
    }

    @Test
    void 가격이_없다면_예외를_던진다() {
        // given, when, then
        assertThatThrownBy(() -> new Money(null))
                .isInstanceOf(PriceIsNotProvidedException.class);
    }

    @Test
    void 돈을_더할_수_있다() {
        // given
        Money money = new Money(BigDecimal.valueOf(1000L));
        Money other = new Money(BigDecimal.valueOf(1001L));

        // when
        Money result = money.add(other);

        // then
        assertThat(result.getValue()).isEqualTo(BigDecimal.valueOf(2001L));
    }

    @Test
    void 대소_여부를_판단할_수_있다() {
        // given
        Money money = new Money(BigDecimal.valueOf(1000L));
        Money other = new Money(BigDecimal.valueOf(1001L));

        // when
        boolean result = money.isGreaterThan(other);

        // then
        assertThat(result).isFalse();
    }
}
