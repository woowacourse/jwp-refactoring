package kitchenpos.application.dto;

public class CreateMenuProductDto {

    private Long productId;
    private Long quantity;

    private CreateMenuProductDto() {
    }

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
