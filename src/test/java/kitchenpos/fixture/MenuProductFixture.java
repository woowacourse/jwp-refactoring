package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct 메뉴상품(Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
