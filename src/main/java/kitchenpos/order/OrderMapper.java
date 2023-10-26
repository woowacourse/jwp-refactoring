package kitchenpos.order;

import kitchenpos.order.request.OrderCreateRequest;
import kitchenpos.order.request.OrderLineItemCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order mapToOrder(OrderCreateRequest request, OrderValidator orderValidator) {
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request.getOrderLineItems());
        return Order.of(
                request.getOrderTableId(),
                orderLineItems,
                orderValidator
        );
    }

    private static List<OrderLineItem> mapToOrderLineItems(List<OrderLineItemCreateRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.of(
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
