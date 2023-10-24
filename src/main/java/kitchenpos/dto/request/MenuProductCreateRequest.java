package kitchenpos.dto.request;

public class MenuProductCreateRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductCreateRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long productId() {
        return productId;
    }

    public Long quantity() {
        return quantity;
    }
}
