package kitchenpos.application.dto;

public class MenuProductDto {

    private final Long productId;
    private final long quantity;

    public MenuProductDto(final long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
