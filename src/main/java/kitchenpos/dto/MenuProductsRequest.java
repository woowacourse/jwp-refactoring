package kitchenpos.dto;

public class MenuProductsRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductsRequest(Long productId, Long quantity) {
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
