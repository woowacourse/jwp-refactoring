package kitchenpos.application.dto;

public class MenuProductCreateRequest {

    private Long productId;
    private int quantity;

    protected MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(final Long productId, final int quantity) {
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
