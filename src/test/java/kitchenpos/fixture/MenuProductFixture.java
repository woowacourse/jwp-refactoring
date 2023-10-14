package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    
    public static MenuProduct MENU_PRODUCT(long productId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1L);
        return menuProduct;
    }
}
