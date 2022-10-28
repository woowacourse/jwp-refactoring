package kitchenpos.dto.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private Long productId;
    private int quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(null, null, productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
