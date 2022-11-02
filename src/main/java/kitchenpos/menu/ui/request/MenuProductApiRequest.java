package kitchenpos.menu.ui.request;

import kitchenpos.menu.application.request.MenuProductRequest;

public class MenuProductApiRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductApiRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest toServiceRequest() {
        return new MenuProductRequest(productId, quantity);
    }
}
