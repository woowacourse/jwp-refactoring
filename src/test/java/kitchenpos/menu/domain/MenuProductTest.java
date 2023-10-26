package kitchenpos.menu.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void calculateSum() {
        // given
        final Product productPriceOne = new Product("product", BigDecimal.ONE);
        final MenuProduct menuProduct = new MenuProduct(productPriceOne, 2L);

        // when & then
        assertThat(menuProduct.calculateSum()).isEqualByComparingTo(BigDecimal.valueOf(2L));
    }
}
