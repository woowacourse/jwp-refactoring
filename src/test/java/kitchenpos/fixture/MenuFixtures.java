package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFixtures {
    public static Menu create(
            String name,
            int price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
