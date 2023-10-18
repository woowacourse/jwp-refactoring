package kitchenpos.application.menu.dto;

public class MenuProductCreateRequest {

    private Long productId;
    private Long quantity;

    private MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(final Long productId, final long quantity) {
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
