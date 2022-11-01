package kitchenpos.menu.dto.application;

public class MenuProductDto {

    private Long productId;
    private long quantity;

    public MenuProductDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
