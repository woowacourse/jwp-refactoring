package kitchenpos.order.dto;

public class OrderLineItemRequest {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private int quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long seq, Long orderId, Long menuId, int quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
