package kitchenpos.application.order.dto.request;

import kitchenpos.core.domain.order.OrderLineItem;

public class OrderLineItemDto {

    private final Long seq;
    private final Long orderId;
    private final Long menuHistoryId;
    private final Integer quantity;

    public OrderLineItemDto(Long seq, Long orderId, Long menuHistoryId, Integer quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuHistoryId = menuHistoryId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuHistoryId(),
                orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuHistoryId() {
        return menuHistoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
