package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

public final class MenuFactory {

    private MenuFactory() {
    }

    public static Menu createMenuOf(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        final Menu menu = createMenuOf(name, price, menuGroupId);
        menu.setId(id);
        return menu;
    }

    public static Menu createMenuOf(final String name, final BigDecimal price, final Long menuGroupId) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(new ArrayList<>());
        return menu;
    }

    public static MenuProduct createMenuProductOf(final Long productId, final long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
