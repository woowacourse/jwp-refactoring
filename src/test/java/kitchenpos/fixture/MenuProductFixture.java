package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId,
        long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static MenuProduct createMenuProductRequest(Long productId, long quantity) {
        return createMenuProduct(null, null, productId, quantity);
    }
}
