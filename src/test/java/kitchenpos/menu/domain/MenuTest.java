package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.vo.PriceIsNegativeException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.MenuPriceIsBiggerThanActualPriceException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴_가격이_0원_미만_이라면_예외를_던진다() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1L);

        // when, then
        assertThatThrownBy(() -> new Menu("name", price, null))
                .isInstanceOf(PriceIsNegativeException.class);
    }

    @Test
    void 메뉴_가격이_0원_이상_이라면_정상_생성_된다() {
        // given
        BigDecimal price = BigDecimal.valueOf(1L);

        // when, then
        assertThatCode(() -> new Menu("name", price, null))
                .doesNotThrowAnyException();
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격_보다_크다면_예외를_던진다() {
        // given
        Menu menu = new Menu("name", BigDecimal.valueOf(1000L), null);
        Product product = new Product("name", BigDecimal.valueOf(500L));

        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(menu, product, 1L));

        // when, then
        assertThatThrownBy(() -> menu.setupMenuProduct(menuProducts))
                .isInstanceOf(MenuPriceIsBiggerThanActualPriceException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격_보다_작다면_메뉴_상품을_등록할_수_있다() {
        // given
        Menu menu = new Menu("name", BigDecimal.valueOf(499L), null);
        Product product = new Product("name", BigDecimal.valueOf(500L));

        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(menu, product, 1L));

        // when
        menu.setupMenuProduct(menuProducts);

        // then
        assertThat(menu.getMenuProducts()).isEqualTo(menuProducts);
    }
}
