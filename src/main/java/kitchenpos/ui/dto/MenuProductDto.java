package kitchenpos.ui.dto;

public class MenuProductDto {

    private Long productId;
    private Long quantity;

    private MenuProductDto() {
    }

    public MenuProductDto(Long productId, Long quantity) {
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
