package kitchenpos.ui.dto;

public class MenuProductDto {

    private Long productId;
    private int quantity;

    public MenuProductDto() {
    }

    public MenuProductDto(final Long productId, final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
