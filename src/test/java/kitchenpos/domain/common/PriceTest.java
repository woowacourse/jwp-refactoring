package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PriceTest {

    @Test
    void 생성자는_유효한_금액을_전달하면_Price를_초기화한다() {
        // when & then
        assertThatCode(() -> new Price(BigDecimal.TEN)).doesNotThrowAnyException();
    }

    @Test
    void 생성자는_유효하지_않은_금액을_전달하면_예외가_발생한다() {
        // given
        final BigDecimal invalidPrice = new BigDecimal(-1);

        // when & then
        assertThatThrownBy(() -> new Price(invalidPrice))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    void plus는_price에_더할_price를_전달하면_그_값을_더한_price를_반환한다() {
        // given
        final Price targetPrice = new Price(BigDecimal.TEN);
        final Price otherPrice = new Price(BigDecimal.TEN);

        // when
        final Price actual = targetPrice.plus(otherPrice);

        // then
        assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(20L));
    }

    @Test
    void times는_price에_곱할_값을_전달하면_그_값을_곱한_price를_반환한다() {
        // given
        final Price targetPrice = new Price(BigDecimal.TEN);

        // when
        final Price actual = targetPrice.times(3);

        // then
        assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(30L));
    }
}
