package kitchenpos.dto.menu;

public class MenuProductCreateRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductCreateRequest(final Long productId,
                                    final Long quantity) {
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
