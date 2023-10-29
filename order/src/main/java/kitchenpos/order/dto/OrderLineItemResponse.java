package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long menuId;
    private final long quantity;

    private OrderLineItemResponse(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity().getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
