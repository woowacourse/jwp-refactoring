package kitchenpos.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.OrderLineItem;

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

    public OrderLineItemRequest(final OrderLineItem orderLineItem) {
        this(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
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
