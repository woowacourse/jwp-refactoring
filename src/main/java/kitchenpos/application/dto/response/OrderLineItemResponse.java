package kitchenpos.application.dto.response;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long menuId;
    private final long quantity;

    private OrderLineItemResponse() {
        this(null, null, 0);
    }

    public OrderLineItemResponse(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
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
