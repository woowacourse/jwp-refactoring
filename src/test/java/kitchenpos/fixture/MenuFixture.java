package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {
    public static Menu createMenu(Long id, String name, Long price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price != null ? BigDecimal.valueOf(price) : null);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static Menu createMenuRequest(String name, Long price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        return createMenu(null, name, price, menuGroupId, menuProducts);
    }
}
