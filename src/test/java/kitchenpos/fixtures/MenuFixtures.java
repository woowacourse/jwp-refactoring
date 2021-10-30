package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
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
        return new Menu(null, name, BigDecimal.valueOf(price * 100, 2), menuGroupId, menuProducts);
    }

    public static Menu createMenu() {
        return createMenu(MENU_NAME, PRICE, MENU_GROUP_ID, createMenuProducts());
    }

    public static Menu createMenu(int price) {
        return createMenu(MENU_NAME, price, MENU_GROUP_ID, createMenuProducts());
    }

    private static List<MenuProduct> createMenuProducts() {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(createMenuProduct());
        menuProducts.add(createMenuProduct());
        return menuProducts;
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

    public static MenuRequest createMenuRequest(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());
    }

    public static MenuRequest createMenuRequest() {
        return createMenuRequest(createMenu());
    }
}
