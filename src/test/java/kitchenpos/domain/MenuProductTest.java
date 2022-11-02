package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void createMenuProduct() {
        // given
        Product product = new Product("상품", BigDecimal.valueOf(1000L));
        long quantity = 2L;
        // when
        MenuProduct menuProduct = new MenuProduct(product, quantity);
        // then
        assertThat(menuProduct).isNotNull();
    }

    @Test
    void getPrice() {
        // given
        MenuProduct menuProduct = new MenuProduct(new Product("상품", BigDecimal.valueOf(1000L)), 2L);
        // when
        Price actual = menuProduct.getPrice();
        // then
        assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(2000L)));
    }
}
