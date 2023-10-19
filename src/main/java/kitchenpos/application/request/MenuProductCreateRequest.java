package kitchenpos.application.request;

public class MenuProductCreateRequest {

    private final Long productId;
    private final Integer quantity;

    public MenuProductCreateRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
