package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct create(long productId, long menuId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1L);

        return menuProduct;
    }
}
