package kitchenpos.support.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuFixture {

    public static Menu createPepperoniMenu(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu("페퍼로니", BigDecimal.valueOf(1000L), menuGroup, menuProducts);
    }
}
