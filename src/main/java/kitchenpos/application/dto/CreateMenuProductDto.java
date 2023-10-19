package kitchenpos.application.dto;

public class CreateMenuProductDto {

    private Long productId;
    private Long quantity;

    public CreateMenuProductDto() {
    }

    public CreateMenuProductDto(Long productId, Long quantity) {
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
