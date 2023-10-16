package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    @Test
    void 메뉴_상품들의_가격의_합을_반환한다() {
        // given
        MenuProduct chicken = new MenuProduct(product("chicken", 1000), 3);
        MenuProduct pizza = new MenuProduct(product("pizza", 2000), 4);
        MenuProducts menuProducts = new MenuProducts(List.of(chicken, pizza));

        // when
        BigDecimal menuProductsPrice = menuProducts.menuProductsPrice();

        // then
        assertThat(menuProductsPrice).isEqualByComparingTo("11000");
    }

}
