package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void validate() {
        // when
        final Price price = new Price(BigDecimal.ONE);

        // then
        assertThat(price.getValue()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void validate_nullException() {
        // when & then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validate_negativeException() {
        // when & then
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
