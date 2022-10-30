package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> from(final OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems()
                .stream()
                .map(OrderLineItemResponse::from)
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
