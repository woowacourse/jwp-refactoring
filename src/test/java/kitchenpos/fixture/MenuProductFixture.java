package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
