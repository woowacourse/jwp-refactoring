package kitchenpos.ui.dto.request;

public class CreateMenuProductRequest {

    private Long productId;
    private Long quantity;

    public CreateMenuProductRequest() {
    }

    public CreateMenuProductRequest(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
