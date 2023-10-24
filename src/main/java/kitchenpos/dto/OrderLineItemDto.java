package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public static OrderLineItemDto from(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public OrderLineItemDto(Long menuId, long quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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
