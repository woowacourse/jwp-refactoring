package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    void calculateSum() {
        // given
        final Product productPriceOne = new Product("product1", BigDecimal.ONE);
        final Product productPriceTen = new Product("product2", BigDecimal.TEN);
        final MenuProduct menuProduct1 = new MenuProduct(productPriceOne, 2L);
        final MenuProduct menuProduct2 = new MenuProduct(productPriceTen, 3L);
        final MenuProducts menuProducts = new MenuProducts(List.of(menuProduct1, menuProduct2));

        // when & then
        assertThat(menuProducts.calculateSum()).isEqualByComparingTo(BigDecimal.valueOf(32L));
    }
}
