package kitchenpos.order.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;

public class OrdersResponse {

    private List<OrderResponse> orderResponses;

    private OrdersResponse() {
    }

    private OrdersResponse(final List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrdersResponse from(final List<Order> orders) {
        List<OrderResponse> ordersResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return new OrdersResponse(ordersResponses);
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
