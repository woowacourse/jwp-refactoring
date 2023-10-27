package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuProducts;
import kitchenpos.menugroup.MenuGroup;

public class MenuFixture {

    public static Menu 메뉴(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        return new Menu(id, name, new Price(price), menuGroup, new MenuProducts(menuProducts));
    }
}
