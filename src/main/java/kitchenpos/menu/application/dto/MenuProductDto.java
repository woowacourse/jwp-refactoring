package kitchenpos.menu.application.dto;

public class MenuProductDto {
    private Long productId;
    private Long quantity;

    public MenuProductDto(final Long productId,
                          final Long quantity) {
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
