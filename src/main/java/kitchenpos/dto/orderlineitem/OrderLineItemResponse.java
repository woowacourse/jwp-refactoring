package kitchenpos.dto.orderlineitem;

import kitchenpos.domain.orderlineitem.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final Long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this(
            orderLineItem.getSeq(),
            orderLineItem.getOrderId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantityValue()
        );
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

    public Long getQuantity() {
        return quantity;
    }
}
