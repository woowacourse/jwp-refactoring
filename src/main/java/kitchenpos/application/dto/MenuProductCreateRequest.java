package kitchenpos.application.dto;

public class MenuProductCreateRequest {

    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductCreateRequest(final Long menuId, final Long productId, final long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
