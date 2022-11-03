package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> from(Order order) {
        return order.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())).collect(
                        Collectors.toList());
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
