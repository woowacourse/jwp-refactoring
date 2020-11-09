package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        Long seq = orderLineItem.getSeq();
        Long orderId = orderLineItem.getOrder().getId();
        Long menuId = orderLineItem.getMenu().getId();
        long quantity = orderLineItem.getQuantity();

        return new OrderLineItemResponse(seq, orderId, menuId, quantity);
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
