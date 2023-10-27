package kitchenpos.dto.request;

public class MenuProductRequest {

    private Long productId;
    private int quantity;

    public MenuProductRequest(Long productId, int quantity) {
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
