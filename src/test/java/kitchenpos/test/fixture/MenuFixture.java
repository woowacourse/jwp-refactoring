package kitchenpos.test.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.vo.MenuProducts;

public class MenuFixture {

    public static Menu 메뉴(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public static Menu 메뉴(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, new MenuProducts(menuProducts));
    }
}
