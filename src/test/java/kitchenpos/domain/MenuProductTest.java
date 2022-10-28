package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
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
    void calculatePrice() {
        // given
        MenuProduct menuProduct = new MenuProduct(new Product("상품", BigDecimal.valueOf(1000L)), 2L);
        // when
        BigDecimal actual = menuProduct.calculatePrice();
        // then
        assertThat(actual).isEqualTo(BigDecimal.valueOf(2000L));
    }
}
