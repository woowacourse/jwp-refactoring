package kitchenpos.ordertable.dto.request;

public class OrderLineRequest {

    private final Long menuId;
    private final long quantity;

    public OrderLineRequest(Long menuId, long quantity) {
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
