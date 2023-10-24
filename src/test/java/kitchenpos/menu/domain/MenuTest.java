package kitchenpos.menu.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.exception.MenuPriceTooExpensiveException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 수량과_가격의_곱보다_메뉴의_가격이_비싸면_예외를_반환한다() {
        // given
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Menu menu = Menu.of("menu", new BigDecimal(10_000), menuGroup.getId(), Collections.emptyList());
        Product product = Product.of("product", new BigDecimal(2500));
        List<MenuProduct> menuProducts = List.of(new MenuProduct(menu, product, 3L));

        // when, then
        assertThrows(MenuPriceTooExpensiveException.class, () -> menu.changeMenuProducts(menuProducts));
    }
}
