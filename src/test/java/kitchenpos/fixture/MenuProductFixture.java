package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct createDefaultWithoutId(final Product product, final Menu menu) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1);
        menuProduct.setProductId(product.getId());
        menuProduct.setMenuId(menu.getId());
        return menuProduct;
    }

    public static MenuProduct createDefaultWithoutId(final Long productId, final Long menuId) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1);
        menuProduct.setProductId(productId);
        menuProduct.setMenuId(menuId);
        return menuProduct;
    }
}
