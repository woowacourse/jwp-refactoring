package kitchenpos.menu.domain;

import java.util.Arrays;
import java.util.List;

public class MenuProducts {
    private final List<MenuProduct> menuProducts;

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(final MenuProduct... menuProducts) {
        this.menuProducts.addAll(Arrays.asList(menuProducts));
    }

    public void addMenuId(final Long menuId) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
        }
    }
    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
