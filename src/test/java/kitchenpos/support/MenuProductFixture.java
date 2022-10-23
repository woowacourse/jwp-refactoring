package kitchenpos.support;

import kitchenpos.domain.MenuProduct;

public enum MenuProductFixture {

    MENU_PRODUCT_1(1);

    private final int quantity;

    MenuProductFixture(final int quantity) {
        this.quantity = quantity;
    }

    public MenuProduct 생성(final long menuId, final long productId) {
        return new MenuProduct(menuId, productId, this.quantity);
    }
}
