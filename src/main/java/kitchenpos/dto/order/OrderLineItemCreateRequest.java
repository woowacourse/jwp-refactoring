package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    protected OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemCreateRequest(OrderLineItem orderLineItem) {
        this(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(this.seq, this.orderId, this.menuId, this.quantity);
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
