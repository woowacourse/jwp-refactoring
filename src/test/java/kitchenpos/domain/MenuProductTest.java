package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {
    @Test
    void amount() {
        final MenuProduct product = new MenuProduct(1L, 10L, BigDecimal.valueOf(1000L));
        assertThat(product.calculateAmount()).isEqualTo(BigDecimal.valueOf(10000L));
    }
}
