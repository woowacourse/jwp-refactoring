package kitchenpos.dto.request;

public class OrderMenuRequest {

    private Long menuId;
    private int quantity;

    public OrderMenuRequest() {
    }

    public OrderMenuRequest(final Long menuId, final int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
