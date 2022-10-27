package kitchenpos.support;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    private static final int QUANTITY = 1;

    public static Menu 메뉴_생성(final String name,
                             final BigDecimal price,
                             final Long menuGroupId,
                             final Product... products) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(makeMenuProducts(products));
        return menu;
    }

    private static List<MenuProduct> makeMenuProducts(final Product[] products) {
        return Arrays.stream(products)
                .map(MenuFixture::convertProductToMenuProduct)
                .collect(Collectors.toList());
    }

    private static MenuProduct convertProductToMenuProduct(final Product product) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(QUANTITY);
        return menuProduct;
    }
}
