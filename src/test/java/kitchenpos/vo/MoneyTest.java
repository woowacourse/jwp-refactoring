package kitchenpos.vo;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

    @Test
    void long을_받아_생성된다() {
        // given
        final long value = 0;

        // when
        final Money money = Money.valueOf(value);

        // then
        assertThat(money.getValue()).isEqualByComparingTo(BigDecimal.valueOf(0));
    }

    @Test
    void BigDecimal을_받아_생성된다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(0);

        // when
        final Money money = Money.valueOf(value);

        // then
        assertThat(money.getValue()).isEqualTo(value);
    }

    @ParameterizedTest(name = "금액이 0보다 작거나 없다면 예외가 발생한다")
    @ValueSource(longs = {-1, -100, Long.MIN_VALUE})
    void 금액이_0보다_작거나_없다면_예외가_발생한다(final long value) {
        // given
        assertThatThrownBy(() -> Money.valueOf(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("금액은 0 이하 혹은 null일 수 없습니다.");
    }

    @Test
    void 상품_가격이_없으면_예외가_발생한다() {
        // given
        assertThatThrownBy(() -> Money.valueOf(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("금액은 0 이하 혹은 null일 수 없습니다.");
    }

    @Test
    void 크기를_비교할_수_있다() {
        // given
        final Money money = Money.valueOf(100);

        // when
        final boolean expected = money.isBiggerThan(Money.valueOf(1));

        // then
        assertThat(expected).isTrue();
    }

}
