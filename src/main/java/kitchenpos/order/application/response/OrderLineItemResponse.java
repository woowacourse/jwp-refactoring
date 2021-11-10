package kitchenpos.order.application.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        final OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse();
        orderLineItemResponse.seq = orderLineItem.getSeq();
        orderLineItemResponse.orderId = orderLineItem.getOrder().getId();
        orderLineItemResponse.menuId = orderLineItem.getMenuId();
        orderLineItemResponse.quantity = orderLineItem.getQuantity();
        return orderLineItemResponse;
    }

    public static List<OrderLineItemResponse> fromList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
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
