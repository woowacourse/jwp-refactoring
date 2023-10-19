package kitchenpos.application.dto.request;

public class MenuProductRequest {

    private final Long productId;
    private final int quantity;

    public MenuProductRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
