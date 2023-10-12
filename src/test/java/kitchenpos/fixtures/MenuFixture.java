package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu MENU(final MenuGroup menuGroup,
                            final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName("kokodak");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);
        menu.setPrice(BigDecimal.valueOf(1000));
        return menu;
    }

    public static Menu MENU(final MenuGroup menuGroup,
                            final List<MenuProduct> menuProducts,
                            final BigDecimal price) {
        final Menu menu = new Menu();
        menu.setName("kokodak");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);
        menu.setPrice(price);
        return menu;
    }
}
