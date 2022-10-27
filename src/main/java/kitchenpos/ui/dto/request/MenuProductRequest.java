package kitchenpos.ui.dto.request;

public class MenuProductRequest {

    private Long productId;
    private int quantity;

    private MenuProductRequest() {
    }

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
