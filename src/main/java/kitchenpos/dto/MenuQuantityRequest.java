package kitchenpos.dto;

public class MenuQuantityRequest {

    private Long menuId;
    private long quantity;

    protected MenuQuantityRequest() {
    }

    public MenuQuantityRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
