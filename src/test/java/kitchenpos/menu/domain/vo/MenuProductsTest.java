package kitchenpos.menu.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    @DisplayName("총 가격을 구한다.")
    void totalPrice() {
        // given
        final MenuProducts menuProducts = new MenuProducts();
        final MenuProduct menuProduct = new MenuProduct(new Product("name", BigDecimal.TEN), 3);
        menuProducts.addAll(List.of(menuProduct));

        // when
        final BigDecimal totalPrice = menuProducts.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(30));
    }
}
