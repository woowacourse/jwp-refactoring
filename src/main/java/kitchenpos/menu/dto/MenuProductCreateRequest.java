package kitchenpos.menu.dto;

public class MenuProductCreateRequest {

    private long quantity;
    private Long productId;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(long quantity, Long productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
