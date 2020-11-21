package kitchenpos.fixture;

import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct createMenuProduct(
        Long seq,
        Long menuId,
        Long productId,
        long quantity
    ) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    public static MenuProductCreateRequest createMenuProductRequest(Long productId, long quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }
}
