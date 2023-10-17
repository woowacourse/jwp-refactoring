package kitchenpos.ui.dto.menu;

public class MenuProductDto {

    private final Long productId;
    private final Integer quantity;

    public MenuProductDto(final Long productId, final Integer quantity) {
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
