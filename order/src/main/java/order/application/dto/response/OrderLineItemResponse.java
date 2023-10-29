package order.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    private OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem, Long orderId) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderId,
                orderLineItem.getOrderMenu().getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public static List<OrderLineItemResponse> from(List<OrderLineItem> orderLineItems, Long orderId) {
        return orderLineItems.stream()
                .map(it -> from(it, orderId))
                .collect(Collectors.toList());
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
