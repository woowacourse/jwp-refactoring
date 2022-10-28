package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void amount() {
        final MenuProduct menuProduct = new MenuProduct(1L, 10, BigDecimal.valueOf(1000L));

        assertThat(menuProduct.getAmount()).isEqualTo(BigDecimal.valueOf(10000L));
    }
}
