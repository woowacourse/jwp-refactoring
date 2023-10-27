package kitchenpos.product.dto;

public class ProductQuantityDto {

    private Long productId;
    private long quantity;

    public ProductQuantityDto(final Long productId, final long quantity) {
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
