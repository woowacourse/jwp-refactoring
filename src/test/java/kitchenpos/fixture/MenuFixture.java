package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public final class MenuFixture {

    public static Menu 메뉴_생성(final Long menuGroupId, final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();

        menu.setPrice(BigDecimal.TEN);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        menu.setName("메뉴");

        return menu;
    }

    public static Menu 메뉴_생성(
            final Long menuGroupId,
            final List<MenuProduct> menuProducts,
            final BigDecimal price,
            final String name
    ) {
        final Menu menu = new Menu();

        menu.setMenuGroupId(menuGroupId);
        menu.setPrice(price);
        menu.setMenuProducts(menuProducts);
        menu.setName(name);

        return menu;
    }

    private MenuFixture() {
    }
}
