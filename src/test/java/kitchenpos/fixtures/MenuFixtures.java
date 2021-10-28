package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFixtures {
    private static final long MENU_ID = 1L;
    private static final long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;
    private static final String MENU_NAME = "기본 메뉴";
    private static final long PRICE = 10000;
    private static final long MENU_GROUP_ID = 1L;

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
        ArrayList<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(createMenuProduct());
        menuProducts.add(createMenuProduct());

        return createMenu(MENU_NAME, PRICE, MENU_GROUP_ID, menuProducts);
    }

    public static Menu createMenu(int price) {
        Menu menu = createMenu();
        menu.setPrice(new BigDecimal(price));
        return menu;
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
}
