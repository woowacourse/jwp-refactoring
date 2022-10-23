package kitchenpos.application.dto;

public class CreateMenuProductDto {

    private final Long productId;
    private final Integer quantity;

    public CreateMenuProductDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
