package kitchenpos.support.fixture.domain;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct getMenuProduct(final Long menuId, final Long productId, final Long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
