package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem, Long orderId) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderId, orderLineItem.getMenuId(),
            orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems, Long orderId) {
        return orderLineItems.stream()
            .map(orderLineItem -> OrderLineItemResponse.of(orderLineItem, orderId))
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
