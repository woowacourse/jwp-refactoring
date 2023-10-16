package kitchenpos.supports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    private static final String DEFAULT_NAME = "상품+상품";
    private static final long DEFAULT_QUANTITY = 2;

    public static Menu of(final Long menuGroupId, final List<Product> products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : products) {
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setQuantity(DEFAULT_QUANTITY);
            menuProduct.setProductId(product.getId());
            menuProducts.add(menuProduct);
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(DEFAULT_QUANTITY)));
        }

        final Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(DEFAULT_NAME);
        menu.setMenuProducts(menuProducts);
        menu.setPrice(totalPrice.subtract(BigDecimal.ONE));
        return menu;
    }
}
