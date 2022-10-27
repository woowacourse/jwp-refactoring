package kitchenpos.dto.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductCreateRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }
}