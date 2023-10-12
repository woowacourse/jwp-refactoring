package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu 메뉴(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu 메뉴(String name, Long price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }
}
