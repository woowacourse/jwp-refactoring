package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void createPrice() {
        // given
        BigDecimal amount = BigDecimal.valueOf(1000);
        // when
        Price price = new Price(amount);
        // then
        assertThat(price).isNotNull();
    }

    @Test
    void createPriceZero() {
        // given
        BigDecimal amount = BigDecimal.valueOf(0);
        // when
        Price price = new Price(amount);
        // then
        assertThat(price).isNotNull();
    }

    @Test
    void createPriceNegative() {
        // given
        BigDecimal amount = BigDecimal.valueOf(-1);
        // when & then
        assertThatThrownBy(() -> new Price(amount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isMoreExpensiveThan() {
        // given
        Price price = new Price(BigDecimal.valueOf(1000));
        // when
        boolean actual = price.isMoreExpensiveThan(BigDecimal.valueOf(800));
        // then
        assertThat(actual).isTrue();
    }

    @Test
    void mulitply() {
        // given
        Price price = new Price(BigDecimal.valueOf(1000));
        // when
        BigDecimal actual = price.multiply(2);
        // then
        assertThat(actual).isEqualTo(BigDecimal.valueOf(1000 * 2));
    }
}
