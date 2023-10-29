package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu menu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }
}
