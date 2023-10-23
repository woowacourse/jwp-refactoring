package kitchenpos.dto.request;

public class MenuProductRequest {
    private Long productId;
    private int quantity;


    public MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final int quantity) {
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
