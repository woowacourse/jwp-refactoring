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
            final var menuProduct = new MenuProduct();
            menuProduct.setQuantity(MENU_QUANTITY);
            menuProduct.setProductId(product.getId());
            menuProducts.add(menuProduct);
            totalPrice += product.getPrice().getPrice().intValue() * menuProduct.getQuantity();
        }

        final var menu = new Menu();
        menu.setName(name);
        menu.setMenuGroupId(group.getId());
        menu.setPrice(new BigDecimal(totalPrice - 1));
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static Menu menu(final String name, final BigDecimal price, final MenuGroup group, final List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (Product product : products) {
            final var menuProduct = new MenuProduct();
            menuProduct.setQuantity(MENU_QUANTITY);
            menuProduct.setProductId(product.getId());
            menuProducts.add(menuProduct);
        }

        final var menu = new Menu();
        menu.setName(name);
        menu.setMenuGroupId(group.getId());
        menu.setPrice(price);
        menu.setMenuProducts(menuProducts);

        return menu;
    }
}
