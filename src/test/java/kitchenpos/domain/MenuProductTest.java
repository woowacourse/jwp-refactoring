package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    void 메뉴_상품의_합친_가격을_반환한다() {
        // given
        MenuProduct menuProduct = new MenuProduct(menu("menu", 1000L, null, List.of()), product("chicken", 1000), 3L);

        // when
        BigDecimal menuPrice = menuProduct.menuPrice();

        // then
        assertThat(menuPrice).isEqualByComparingTo("3000");
    }
}
