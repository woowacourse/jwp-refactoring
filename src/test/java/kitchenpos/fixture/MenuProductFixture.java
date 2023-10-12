package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct menuProduct(final Long productId, final long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static MenuProduct menuProduct(final Long menuId, final MenuProduct menuProduct) {
        menuProduct.setMenuId(menuId);

        return menuProduct;
    }
}
