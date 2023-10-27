package kitchenpos.common.event;

public class ProductQuantityEventDto {

    private Long productId;
    private long quantity;

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
