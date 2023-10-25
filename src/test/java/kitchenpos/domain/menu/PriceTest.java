package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    @DisplayName("가격이 null이면 예외가 발생한다")
    void price_nullPrice() {
        // given
        final BigDecimal invalidPriceValue = null;

        // when & then
        assertThatThrownBy(() -> new Price(invalidPriceValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("가격이 음수이면 예외가 발생한다")
    void price_negativePrice() {
        // given
        final BigDecimal invalidPriceValue = BigDecimal.valueOf(-1);

        // when & then
        assertThatThrownBy(() -> new Price(invalidPriceValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("두 가격을 더한다")
    void add() {
        // given
        final Price price1 = new Price(new BigDecimal(1000));
        final Price price2 = new Price(new BigDecimal(2000));

        final Price expect = new Price(new BigDecimal(3000));

        // when
        final Price actual = price1.add(price2);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("개수가 주어지면 가격과 개수의 곱을 구한다")
    void multiply() {
        // given
        final Price price = new Price(BigDecimal.valueOf(10000));
        final int quantity = 3;

        final Price expect = new Price(BigDecimal.valueOf(30000));

        // when
        final Price actual = price.multiply(quantity);

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
