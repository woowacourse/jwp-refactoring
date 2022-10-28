package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFactory {

    public static int MENU_QUANTITY = 2;

    public static Menu menu(final String name, final MenuGroup group, final List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        var totalPrice = 0;

        for (Product product : products) {
            final var menuProduct = new MenuProduct(null, product, MENU_QUANTITY);
            menuProducts.add(menuProduct);
            totalPrice += product.getPrice().intValue() * menuProduct.getQuantity();
        }

        return new Menu(null, name, new BigDecimal(totalPrice - 1), group.getId(), menuProducts);
    }

    public static Menu menu(final String name, final BigDecimal price, final MenuGroup group,
                            final List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (Product product : products) {
            final var menuProduct = new MenuProduct(null, null, product.getId(), MENU_QUANTITY);
            menuProducts.add(menuProduct);
        }

        return new Menu(null, name, price, group.getId(), menuProducts);
    }
}
