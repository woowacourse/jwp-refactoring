package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts, BigDecimal price) {
        validateMenuProducts(menuProducts, price);
        return new MenuProducts(menuProducts);
    }

    private static void validateMenuProducts(List<MenuProduct> menuProducts, BigDecimal price) {
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::getMenuPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }
}
