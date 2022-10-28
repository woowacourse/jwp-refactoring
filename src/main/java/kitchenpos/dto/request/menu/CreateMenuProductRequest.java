package kitchenpos.dto.request.menu;

public class CreateMenuProductRequest {

    private Long productId;
    private long quantity;

    public CreateMenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
