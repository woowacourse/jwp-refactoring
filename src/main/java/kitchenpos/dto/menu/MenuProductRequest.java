package kitchenpos.dto.menu;

public class MenuProductRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductRequest() {
        this(null, null);
    }

    public MenuProductRequest(Long productId, Long quantity) {
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
