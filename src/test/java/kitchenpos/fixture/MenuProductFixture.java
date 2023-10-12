package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long menuId, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
