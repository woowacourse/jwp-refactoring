package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuFixture {
    private static final String NAME = "육회초밥";
    private static final BigDecimal PRICE = BigDecimal.valueOf(15900);
    private static final Long MENU_GROUP_ID = 1L;

    public static Menu createMenu() {
        return createMenu(NAME, PRICE, MENU_GROUP_ID, Collections.emptyList());
    }

    public static Menu createMenu(Long id) {
        return createMenu(id, NAME, PRICE, MENU_GROUP_ID, Collections.emptyList());
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return createMenu(null, name, price, menuGroupId,menuProducts);
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
