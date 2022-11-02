package kitchenpos.ui.dto;

public class MenuProductRequest {

    private long productId;
    private int quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
