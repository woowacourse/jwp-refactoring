package kitchenpos.order.ui.response;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderId;
    private final Long orderMenuId;
    private final Long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, Long orderMenuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.orderMenuId = orderMenuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getOrderMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public static List<OrderLineItemResponse> toList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
