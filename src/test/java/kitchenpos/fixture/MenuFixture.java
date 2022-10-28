package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu createMenu(Long id) {
        return new Menu(id);
    }

    public static Menu createMenu(String name, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, menuGroupId, menuProducts);
    }

    public static Menu createMenu(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static Menu createMenu(Long id, String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }
}
