package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu 메뉴_생성(
            final String name, final int price, final Long menuGroupId, final List<MenuProduct>menuProducts
    ) {
        return 메뉴_생성(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static Menu 메뉴_생성(
            final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct>menuProducts
    ) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
