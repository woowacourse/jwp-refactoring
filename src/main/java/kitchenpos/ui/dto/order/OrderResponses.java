package kitchenpos.ui.dto.order;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponses {

    private final List<OrderResponse> orders;

    public OrderResponses(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrderResponses from(List<Order> orders) {
        return new OrderResponses(
                orders.stream()
                    .map(OrderResponse::from)
                    .collect(Collectors.toList())
        );
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
