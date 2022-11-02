package kitchenpos.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemChangeResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemChangeResponse() {
    }

    public OrderLineItemChangeResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemChangeResponse> from(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemChangeResponse::from)
                .collect(Collectors.toList());
    }

    private static OrderLineItemChangeResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemChangeResponse(orderLineItem.getSeq(), orderLineItem.getOrder().getId(),
                orderLineItem.getMenuId(), orderLineItem.getQuantity());
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
