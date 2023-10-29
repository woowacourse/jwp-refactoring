package order.dto;

import order.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class ListOrderResponse {
    private final List<OrderResponse> orders;

    private ListOrderResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static ListOrderResponse of(List<Order> orders) {
        return new ListOrderResponse(orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList()));
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
