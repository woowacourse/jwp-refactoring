package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.exception.MenuPriceIsBiggerThanActualPriceException;
import kitchenpos.vo.Money;
import kitchenpos.vo.PriceIsNegativeException;
import org.assertj.core.api.Assertions;
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
        Assertions.assertThatThrownBy(() -> new Menu(null, "name", price))
                .isInstanceOf(PriceIsNegativeException.class);
    }

    @Test
    void 메뉴_가격이_0원_이상_이라면_정상_생성_된다() {
        // given
        BigDecimal price = BigDecimal.valueOf(1L);

        // when, then
        Assertions.assertThatCode(() -> new Menu(null, "name", price))
                .doesNotThrowAnyException();
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격_보다_크다면_예외를_던진다() {
        // given
        Menu menu = new Menu(null, "name", BigDecimal.valueOf(1000L));
        Product product = new Product("name", BigDecimal.valueOf(500L));

        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(product.getId(), 1L,
                        new Money(product.getPrice().multiply(BigDecimal.valueOf(1L)))));

        // when, then
        Assertions.assertThatThrownBy(() -> menu.setupMenuProducts(menuProducts))
                .isInstanceOf(MenuPriceIsBiggerThanActualPriceException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격_보다_작다면_메뉴_상품을_등록할_수_있다() {
        // given
        Menu menu = new Menu(null, "name", BigDecimal.valueOf(499L));
        Product product = new Product("name", BigDecimal.valueOf(500L));

        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(product.getId(), 1L,
                        new Money(product.getPrice().multiply(BigDecimal.valueOf(1L)))));

        // when
        menu.setupMenuProducts(menuProducts);

        // then
        assertThat(menu.getMenuProducts()).isEqualTo(menuProducts);
    }
}
