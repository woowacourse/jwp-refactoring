package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴의_가격은_0원_보다_작으면_예외가_발생한다() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1);

        // expect
        assertThatThrownBy(() -> new Menu("menu", price, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이여야합니다");
        ;
    }

    @Test
    void 메뉴의_가격이_메뉴_상품들의_합과_다르면_예외가_발생한다() {
        // given
        Product product = product("product", 1000L);
        MenuProduct menuProduct = menuProduct(product, 1L);

        // expect
        assertThatThrownBy(() -> new Menu("product", BigDecimal.valueOf(1001), 1L, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격의 합이 맞지 않습니다");
    }
}
