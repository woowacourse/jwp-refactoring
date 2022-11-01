package kitchenpos.order.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderLineItemRequest {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    @JsonCreator
    public OrderLineItemRequest(final Long seq, final Long orderId, final Long menuId, final long quantity) {
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

    public long getQuantity() {
        return quantity;
    }
}
