package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixtures {

    public static final MenuProduct generateMenuProduct(final Long productId, final long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
