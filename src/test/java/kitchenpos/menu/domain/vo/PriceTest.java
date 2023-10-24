package kitchenpos.menu.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("가격을 비교한다.")
    void compareTo() {
        // given
        final Price price = new Price(BigDecimal.ZERO);

        // when
        final int compareTo = price.compareTo(BigDecimal.TEN);

        // then
        assertThat(compareTo).isEqualTo(-1);
    }

    @Test
    @DisplayName("가격을 곱한다.")
    void multiply() {
        // given
        final Price price = new Price(BigDecimal.TEN);

        // when
        final BigDecimal multiply = price.multiply(BigDecimal.valueOf(3));

        // then
        assertThat(multiply).isEqualTo(BigDecimal.valueOf(30));
    }
}
