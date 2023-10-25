package kitchenpos.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuFixture {

    public static Menu menu(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static Menu menu(String name, Long price, Long menuGroupId) {
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroupId, new ArrayList<>());
    }
}
