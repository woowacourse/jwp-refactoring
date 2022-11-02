package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.menu.MenuProduct;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void createMenuProduct() {
        // given
        Quantity quantity = new Quantity(2L);
        Price price = new Price(BigDecimal.valueOf(1000L));
        // when
        MenuProduct menuProduct = new MenuProduct(1L, price, quantity);
        // then
        assertThat(menuProduct).isNotNull();
    }

    @Test
    void calculateTotalPrice() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, new Price(BigDecimal.valueOf(2000L)), new Quantity(2L));
        // when
        Price actual = menuProduct.calculateTotalPrice();
        // then
        assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(4000L)));
    }
}
