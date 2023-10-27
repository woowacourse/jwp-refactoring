package kitchenpos.ui.response;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final Long quantity;

    private OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> from(final List<OrderLineItem> orderLineItems, final Order order) {
        return orderLineItems.stream().map(it -> new OrderLineItemResponse(
                it.getSeq(),
                order.getId(),
                it.getMenuId(),
                it.getQuantity()
        )).collect(Collectors.toList());
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

    public Long getQuantity() {
        return quantity;
    }
}
