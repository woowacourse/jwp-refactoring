package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.product.Product;
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

        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product1, 2), new MenuProduct(product2, 1)));

        // when
        final BigDecimal result = menuProducts.getTotalPrice();

        // then
        assertThat(result).isEqualTo(new BigDecimal(90000));
    }

    @DisplayName("메뉴 상품을 메뉴와 연관관계를 맺는다.")
    @Test
    void join() {
        // given
        final Menu menu = new Menu();
        final Product product1 = new Product("상품1", 20000);
        final Product product2 = new Product("상품2", 50000);

        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product1, 2), new MenuProduct(product2, 1)));

        // when
        final MenuProducts result = menuProducts.join(menu);

        // then
        assertThat(result.getProducts().get(0).getMenu()).isNotNull();
        assertThat(result.getProducts().get(1).getMenu()).isNotNull();
    }
}
