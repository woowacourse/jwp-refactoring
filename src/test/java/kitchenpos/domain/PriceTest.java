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
}
