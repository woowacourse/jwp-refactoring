package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    @DisplayName("메뉴 상품의 총 금액을 구한다.")
    @Test
    void get_total_Price() {
        // given
        final Product product1 = new Product("상품1", 20000);
        final Product product2 = new Product("상품2", 50000);
        final Menu menu = new Menu("메뉴", 30000, 1L);

        final MenuProducts menuProducts = new MenuProducts(List.of
            (new MenuProduct(product1, menu, 2),
                new MenuProduct(product2, menu, 1)));

        // when
        final BigDecimal result = menuProducts.getTotalPrice();

        // then
        assertThat(result).isEqualTo(new BigDecimal(90000));
    }
}
