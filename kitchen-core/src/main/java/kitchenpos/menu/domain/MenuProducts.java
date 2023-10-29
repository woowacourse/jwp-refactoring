package kitchenpos.menu.domain;

import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    private MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(final Price price, final List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        return new MenuProducts(menuProducts);
    }

    private static void validatePrice(final Price price, final List<MenuProduct> menuProducts) {
        final Price totalPrice = calculateTotalPrice(menuProducts);
        if (price.isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException();
        }
    }

    private static Price calculateTotalPrice(final List<MenuProduct> menuProducts) {
        Price totalPrice = Price.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            totalPrice = totalPrice.add(menuProduct.calculatePrice());
        }
        return totalPrice;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
