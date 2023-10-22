package kitchenpos.domain.menu;

import static kitchenpos.exception.MenuException.NoMenuProductsException;
import static kitchenpos.fixture.MenusFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuProductsTest {

    @Test
    void 메뉴_상품_들은_메뉴_상품의_총_가격을_계산한다() {
        // given
        final MenuProduct menuProduct1 = 메뉴_상품(상품("상품1", BigDecimal.valueOf(100)), 10L);
        final MenuProduct menuProduct2 = 메뉴_상품(상품("상품2", BigDecimal.valueOf(1000)), 10L);

        // when
        final MenuProducts menuProducts = new MenuProducts(List.of(menuProduct1, menuProduct2));
        final Price totalPrice = menuProducts.getTotalPrice();

        // then
        assertThat(totalPrice)
                .usingRecursiveComparison()
                .isEqualTo(new Price(BigDecimal.valueOf(11000)));
    }

    @Test
    void 메뉴_상품_들은_없을_수_없다() {
        // given
        final List<MenuProduct> menuProducts = null;

        // expected
        assertThatThrownBy(() -> new MenuProducts(menuProducts))
                .isInstanceOf(NoMenuProductsException.class);
    }

    @Test
    void 메뉴_상품_들은_비어_있을_수_없다() {
        // given
        final List<MenuProduct> menuProducts = Collections.emptyList();

        // expected
        assertThatThrownBy(() -> new MenuProducts(menuProducts))
                .isInstanceOf(NoMenuProductsException.class);
    }
}
