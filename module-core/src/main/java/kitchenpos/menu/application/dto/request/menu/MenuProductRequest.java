package kitchenpos.menu.application.dto.request.menu;

import kitchenpos.menu.domain.Quantity;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return new Quantity(quantity);
    }
}
