package kitchenpos.order.service.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final long seq;
    private final long orderId;
    private final long menuId;
    private final long quantity;

    private OrderLineItemResponse(final long seq, final long orderId, final long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> from(final List<OrderLineItem> orderLineItems, final Long orderId) {
        return orderLineItems.stream().map(each -> new OrderLineItemResponse(
                each.getSeq(),
                orderId,
                each.getMenuId(),
                each.getQuantity()
        )).collect(Collectors.toList());
    }

    public long getSeq() {
        return seq;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
