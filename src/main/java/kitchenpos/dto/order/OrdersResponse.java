package kitchenpos.dto.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrdersResponse {

    private final List<OrderResponse> orders;

    private OrdersResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrdersResponse from(final List<Order> orders) {
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new OrdersResponse(orderResponses);
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
