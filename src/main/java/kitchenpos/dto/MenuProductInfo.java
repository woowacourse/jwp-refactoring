package kitchenpos.dto;

public class MenuProductInfo {

    private final Long productId;
    private final Long quantity;

    public MenuProductInfo(Long productId, Long quantity) {
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
