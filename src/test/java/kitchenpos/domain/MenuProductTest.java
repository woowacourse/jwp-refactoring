package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void calculateMultiplyPrice() {
        final MenuProduct menuProduct = new MenuProduct(1L, 3L, BigDecimal.valueOf(32000));

        assertThat(menuProduct.calculateMultiplyPrice()).isEqualTo(BigDecimal.valueOf(96000L));
    }
}
