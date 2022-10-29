package kitchenpos.application.dto.request;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemRequest(final Long orderId, final Long menuId, final long quantity) {
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
