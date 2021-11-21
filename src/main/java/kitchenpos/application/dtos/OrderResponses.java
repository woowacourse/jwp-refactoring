package kitchenpos.application.dtos;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrderResponses {
    private final List<OrderResponse> orderResponses;

    public OrderResponses(List<Order> orders) {
        this.orderResponses = orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
