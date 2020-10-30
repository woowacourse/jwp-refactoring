package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class MenuFixture {

    private static final String MENU_NAME = "후라이드 치킨";

    private static Menu createMenu(Long id, Long groupId, BigDecimal price, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(MENU_NAME);
        menu.setMenuGroupId(groupId);
        menu.setPrice(price);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createMenuWithoutId(Long groupId, BigDecimal price, MenuProduct menuProducts) {
        return createMenu(null, groupId, price, Arrays.asList(menuProducts));
    }

    public static Menu createMenuWithPrice(BigDecimal price) {
        return createMenu(null, null, price, null);
    }

    public static Menu createMenuWithGroupId(Long groupId) {
        return createMenu(null, groupId, BigDecimal.ONE, null);
    }


}
