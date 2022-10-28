package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrdersResponse {

    private final List<OrderResponse> orderResponses;

    public OrdersResponse(final List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }

    public static OrdersResponse of(final List<Order> orders) {
        final List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());

        return new OrdersResponse(orderResponses);
    }
}
