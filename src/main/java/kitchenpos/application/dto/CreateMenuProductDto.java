package kitchenpos.application.dto;

public class CreateMenuProductDto {

    private final Long productId;
    private final Long quantity;

    public CreateMenuProductDto(final Long productId, final Long quantity) {
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
