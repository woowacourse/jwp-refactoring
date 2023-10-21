package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createMenuProduct(final Long productId, final int quantity, final Long menuId) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(quantity);
        menuProduct.setMenuId(menuId);

        return menuProduct;
    }
}
