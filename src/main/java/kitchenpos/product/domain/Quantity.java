package kitchenpos.product.domain;

public class Quantity {

    private final Long productId;
    private final Long quantity;

    public Quantity(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public boolean isSameProductId(final Long id) {
        return productId.equals(id);
    }

    public Long getQuantity() {
        return quantity;
    }
}
