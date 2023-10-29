package kitchenpos.event;

public class ProductQuantityEventDto {

    private final Long productId;
    private final long quantity;

    public ProductQuantityEventDto(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
