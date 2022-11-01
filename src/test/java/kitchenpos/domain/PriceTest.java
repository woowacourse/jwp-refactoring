package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.menu.Price;

class PriceTest {

    @DisplayName("가격이 null일 경우 예외가 발생한다")
    @Test
    void validateNullPrice() {
        BigDecimal price = null;

        assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수일 경우 예외가 발생한다")
    @Test
    void validateNegativePrice() {
        BigDecimal price = new BigDecimal(-1);

        assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("금액을 비교하여 클 경우 true를 반환한다")
    @Test
    void isMoreExpensive() {
        Price price = new Price(new BigDecimal(20_000));
        BigDecimal comparisonTarget = new BigDecimal(15_000);

        boolean actual = price.isMoreExpensive(comparisonTarget);
        assertThat(actual).isTrue();
    }
}
