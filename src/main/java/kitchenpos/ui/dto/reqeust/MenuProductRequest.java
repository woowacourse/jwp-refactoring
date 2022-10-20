package kitchenpos.ui.dto.reqeust;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final Long quantity) {
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
