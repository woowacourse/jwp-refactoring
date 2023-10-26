package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {
    
    public static Menu MENU(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(name,
                BigDecimal.valueOf(price),
                menuGroup,
                menuProducts);
    }
}
