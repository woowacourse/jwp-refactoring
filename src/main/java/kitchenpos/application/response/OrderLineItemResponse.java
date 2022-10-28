package kitchenpos.application.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(final OrderLineItem orderLineItem) {
        this(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    @JsonCreator
    public OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
