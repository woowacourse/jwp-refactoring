package kitchenpos.dto.request;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(Long menuId, long quantity) {
        this(null, menuId, quantity);
    }

    public OrderLineItemRequest(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
