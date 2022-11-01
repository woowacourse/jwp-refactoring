package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @DisplayName("메뉴에 포함된 상품의 총 정가를 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        final Product product_13000 = Product.ofUnsaved("피자", BigDecimal.valueOf(13000));
        final Product product_4500 = Product.ofUnsaved("버팔로윙", BigDecimal.valueOf(4500));
        final MenuProduct menuProduct_26000 = MenuProduct.ofUnsaved(null, product_13000, 2);
        final MenuProduct menuProduct_4500 = MenuProduct.ofUnsaved(null, product_4500, 1);

        final MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct_26000, menuProduct_4500));

        // when
        final Price price = menuProducts.calculateTotalPrice();

        assertThat(price).isEqualTo(Price.from(30500));
    }
}
