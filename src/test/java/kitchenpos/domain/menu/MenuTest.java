package kitchenpos.domain.menu;

import java.util.List;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("menu 가격이 상품들의 가격합보다 더 크면 예외를 발생한다.")
    @Test
    void validate_price_isBigger_than_products_price() {
        // given
        final Product product = new Product("상품", 100);
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 10)));

        // when
        // then
        assertThatThrownBy(() -> new Menu("메뉴", 30000, 1L, menuProducts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 금액은 상품들의 금액 합보다 클 수 없습니다.");
    }
}
