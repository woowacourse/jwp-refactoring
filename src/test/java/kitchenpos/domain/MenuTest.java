package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 가격을 계산한다.")
    @Test
    void calculatePrice() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, new Product(1L, "상품1", BigDecimal.valueOf(1000L)), 1L);
        final MenuProduct menuProduct2 = new MenuProduct(2L, new Product(2L, "상품2", BigDecimal.valueOf(2000L)), 2L);

        // when
        final Menu menu = new Menu(1L, "메뉴", List.of(menuProduct1, menuProduct2), 1L);

        // then
        assertThat(menu.getPrice()).isEqualTo(new Price(BigDecimal.valueOf(5000L)));
    }

    @DisplayName("메뉴에 메뉴 상품을 등록한다.")
    @Test
    void addMenuProduct() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, new Product(1L, "상품", BigDecimal.TEN), 1L);

        // when
        final Menu menu = new Menu(1L, "메뉴", List.of(menuProduct), 1L);

        // then
        assertThat(menu.getMenuProducts()).contains(menuProduct);
    }
}
