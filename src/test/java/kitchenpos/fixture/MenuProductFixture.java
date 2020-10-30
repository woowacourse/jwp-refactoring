package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createMenuProductWithoutId(Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(null);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
