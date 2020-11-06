package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {
    public static final Long ID1 = 1L;
    public static final String NAME = "chicken";

    public static Menu createWithoutId(Long menuGroupId, List<MenuProduct> products, Long price) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(products);
        menu.setName(NAME);
        menu.setPrice(BigDecimal.valueOf(price));

        return menu;
    }

    public static Menu createWithId(Long id, Long menuGroupId, List<MenuProduct> products,
        Long price) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(products);
        menu.setName(NAME);
        menu.setPrice(BigDecimal.valueOf(price));

        return menu;
    }
}
