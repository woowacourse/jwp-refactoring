package kitchenpos.application.dto.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, quantity);
    }
}
