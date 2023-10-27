package kitchenpos.order.application.dto.response;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemQueryResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemQueryResponse(final Long seq, final Long orderId, final Long menuId,
                                      final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemQueryResponse() {
    }

    public static OrderLineItemQueryResponse of(final Long orderId, final OrderLineItem orderLineItem) {
        return new OrderLineItemQueryResponse(orderLineItem.getSeq(), orderId,
                orderLineItem.getMenuId(), orderLineItem.getQuantity());
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
