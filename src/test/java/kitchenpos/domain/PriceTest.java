package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void validate() {
        assertThat(new Price(10000).getPrice())
                .isEqualTo(new BigDecimal("10000.0"));
    }

    @Test
    void validate_error() {
        assertThatThrownBy(() -> new Price(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0 이상이어야 한다.");
    }
}
