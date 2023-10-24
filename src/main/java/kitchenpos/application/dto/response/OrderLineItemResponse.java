package kitchenpos.application.dto.response;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final long quantity;
    private final Long menuId;

    public OrderLineItemResponse(final Long seq, final long quantity, final Long menuId) {
        this.seq = seq;
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getQuantity(),
                orderLineItem.getMenu().getId()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }
}
