package kitchenpos.dto;

public class MenuProductsRequest {

    private Long productId;
    private Long quantity;

    public MenuProductsRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductsRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
