package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFixtures {

    private static final Long MENU_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;
    private static final String MENU_NAME = "기본 메뉴";
    private static final String MENU_GROUP_NAME = "기본 메뉴 그룹";
    private static final long PRICE = 10000;
    private static final Long MENU_GROUP_ID = 1L;
    private static final List<MenuProduct> MENU_PRODUCTS = Collections.singletonList(createMenuProduct());

    public static Menu createMenu(
        String name,
        long price,
        Long menuGroupId,
        List<MenuProduct> menuProducts
    ) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price * 100, 2));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createMenu() {
        return createMenu(MENU_NAME, PRICE, MENU_GROUP_ID, MENU_PRODUCTS);
    }

    public static MenuProduct createMenuProduct(
        Long menuId,
        Long productId,
        long quantity
    ) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProduct createMenuProduct() {
        return createMenuProduct(MENU_ID, PRODUCT_ID, QUANTITY);
    }

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static MenuGroup createMenuGroup() {
        return createMenuGroup(MENU_GROUP_NAME);
    }
}
