package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 가격이_0미만_이면_예외가_발생한다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(-1);

        // expected
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0이상 이어야 합니다.");
    }

    @Test
    void 입력한_값보다_크면_true를_반환한다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(10000);
        final Price price = new Price(BigDecimal.valueOf(10001));

        // when
        final boolean result = price.isGreaterThan(value);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 입력한_값보다_작으면_false를_반환한다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(10001);
        final Price price = new Price(BigDecimal.valueOf(10000));

        // when
        final boolean result = price.isGreaterThan(value);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 입력한_값과_곱한_값을_반환한다() {
        // given
        final long other = 2;
        final Price price = new Price(BigDecimal.valueOf(10000));

        // when
        final BigDecimal result = price.multiply(other);

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(20000));
    }
}
