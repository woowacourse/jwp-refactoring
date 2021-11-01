package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuFixture {
    private static final String MENU_NAME = "육회초밥";
    private static final String MENU_GROUP_NAME = "나혼자세트";
    private static final BigDecimal PRICE = BigDecimal.valueOf(15900);
    private static final Long MENU_GROUP_ID = 1L;

    public static MenuGroup createMenuGroup() {
        return createMenuGroup(null);
    }

    public static MenuGroup createMenuGroup(Long id) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(MENU_GROUP_NAME);
        return menuGroup;
    }

    public static Menu createMenu() {
        return createMenu(MENU_NAME, PRICE, MENU_GROUP_ID, Collections.emptyList());
    }

    public static Menu createMenu(Long id) {
        return createMenu(id, MENU_NAME, PRICE, MENU_GROUP_ID, Collections.emptyList());
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return createMenu(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public static MenuProduct createMenuProduct(Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1L);
        return menuProduct;
    }

    public static MenuProduct createMenuProduct(Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
