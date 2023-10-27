package kitchenpos.menu.application.dto;

public class MenuProductQuantityDto {

    private Long productId;
    private long quantity;

    public MenuProductQuantityDto(Long productId, long quantity) {
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
