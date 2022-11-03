package kitchenpos.support;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public enum MenuProductFixture {

    MENU_PRODUCT_1(1);

    private final int quantity;

    MenuProductFixture(final int quantity) {
        this.quantity = quantity;
    }

    public MenuProduct 생성(final Product product) {
        return new MenuProduct(null, product, this.quantity);
    }
}
