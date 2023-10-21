package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu 메뉴(
            final Long menuGroupId,
            final String name,
            final List<MenuProduct> menuProductList,
            final BigDecimal price
    ) {
        final Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(name);
        menu.setMenuProducts(menuProductList);
        menu.setPrice(price);
        return menu;
    }

    public static Menu 메뉴(
            final Long menuGroupId,
            final String name,
            final BigDecimal price
    ) {
        final Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(name);
        menu.setPrice(price);
        return menu;
    }
}
