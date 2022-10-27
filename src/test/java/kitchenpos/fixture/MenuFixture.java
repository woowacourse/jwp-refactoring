package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, MenuProduct... menuProducts) {
        return new Menu(name, price, menuGroupId, List.of(menuProducts));
    }
}
