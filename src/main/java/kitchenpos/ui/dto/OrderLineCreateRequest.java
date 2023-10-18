package kitchenpos.ui.dto;

public class OrderLineCreateRequest {

    private Long menuId;
    private long quantity;

    private OrderLineCreateRequest() {

    }

    public OrderLineCreateRequest(Long menuId, long quantity) {
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
