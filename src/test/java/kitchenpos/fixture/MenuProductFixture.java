package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.request.MenuProductCreateRequest;

public class MenuProductFixture {
    public static int DEFAULT_QUANTITY = 1;

    public static MenuProduct create(Long menuId, Long productId) {
        return new MenuProduct(menuId, productId, DEFAULT_QUANTITY);
    }

    public static MenuProductCreateRequest createRequest(Long productId, int quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }
}
