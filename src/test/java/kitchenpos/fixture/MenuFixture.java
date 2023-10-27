package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuFixture {

    public static Menu 메뉴(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        return new Menu(id, name, new Price(price), menuGroup, new MenuProducts(menuProducts));
    }
}
