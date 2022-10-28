package kitchenpos.application.dto.request;

public class MenuProductCreateRequest {
    private final Long productId;
    private final long quantity;

    private MenuProductCreateRequest() {
        this(null, 0L);
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
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
