package kitchenpos.domain;

import kitchenpos.dto.request.order.OrderLineItemsDto;

public class OrderLineItem {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItem(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem from(final OrderLineItemsDto dto) {
        return new OrderLineItem(
                dto.getSeq(),
                dto.getOrderId(),
                dto.getMenuId(),
                dto.getQuantity()
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

    public long getQuantity() {
        return quantity;
    }
}
