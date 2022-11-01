package kitchenpos.application.menu;

public class MenuProductRequest {

    private Long productId;
    private Long quantity;

    private MenuProductRequest() {
    }

    MenuProductRequest(Long productId, Long quantity) {
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
