package kitchenpos.application.response;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseAssembler {

    public List<OrderResponse> orderResponses(final List<Order> orders) {
        return orders.stream()
                .map(this::orderResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderResponse orderResponse(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponses(order.getOrderLineItems())
        );
    }

    private List<OrderLineItemResponse> orderLineItemResponses(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(
                        orderLineItem.getSeq(),
                        orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))
                .collect(Collectors.toUnmodifiableList());
    }
}
