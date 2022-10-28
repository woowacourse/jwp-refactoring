package kitchenpos.ui.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {

    private  Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemDto() {
    }

    public OrderLineItemDto(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(seq,orderId,menuId,quantity);
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
