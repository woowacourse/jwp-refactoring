package kitchenpos.core.dto.request;

public class MenuProductCreateRequest {

    private Long productId;
    private Long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(final Long productId,
                                    final Long quantity) {
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
