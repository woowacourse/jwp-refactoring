package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrdersResponse {

    private List<OrderResponse> orderResponses;

    private OrdersResponse() {
    }

    private OrdersResponse(final List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrdersResponse of(final List<Order> orders) {
        List<OrderResponse> ordersResponses = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());

        return new OrdersResponse(ordersResponses);
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
