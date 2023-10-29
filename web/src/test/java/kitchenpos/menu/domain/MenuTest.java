package kitchenpos.menu.domain;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("menu 가격이 상품들의 가격합보다 더 크면 예외를 발생한다.")
    @Test
    void validate_price_isBigger_than_products_price() {
        // given
        final Product product = new Product("상품", 100);
        final Menu menu = new Menu("메뉴", 2000, 1L);
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, menu, 10)));

        // when
        // then
        assertThatThrownBy(() -> menu.validateOverPrice(menuProducts.getTotalPrice()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 금액은 상품들의 금액 합보다 클 수 없습니다.");
    }
}
