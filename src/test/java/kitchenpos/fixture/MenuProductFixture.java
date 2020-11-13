package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.request.MenuProductCreateRequest;

public class MenuProductFixture {
    public static int DEFAULT_QUANTITY = 1;

    public static MenuProduct create(Long menuId, Long productId) {
        MenuProduct menuProduct = new MenuProduct();

        menuProduct.setProductId(productId);
        menuProduct.setMenuId(menuId);
        menuProduct.setQuantity(DEFAULT_QUANTITY);

        return menuProduct;
    }

    public static MenuProductCreateRequest createRequest(Long productId, int quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }
}
