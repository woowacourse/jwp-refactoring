package kitchenpos.ui.dto;

public class MenuProductDto {

    private Long productId;
    private Integer quantity;

    private MenuProductDto() {
    }

    public MenuProductDto(Long productId, Integer quantity) {
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
