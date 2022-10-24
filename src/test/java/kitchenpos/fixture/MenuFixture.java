package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    private static final int ONE_QUANTITY = 1;

    public static Menu createMenu(final String name,
                                  final int price,
                                  final MenuGroup menuGroup,
                                  final Product... products) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroup.getId());
        setMenuProducts(menu, products);
        return menu;
    }

    private static void setMenuProducts(final Menu menu, final Product[] products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Product product : products) {
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(ONE_QUANTITY);
            menuProducts.add(menuProduct);
        }
        menu.setMenuProducts(menuProducts);
    }
}
