package kitchenpos.application.common.factory;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFactory {

    private MenuFactory() {
    }

    public static Menu create(
        String name,
        BigDecimal price,
        MenuGroup menuGroup,
        List<MenuProduct> menuProducts
    ) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
