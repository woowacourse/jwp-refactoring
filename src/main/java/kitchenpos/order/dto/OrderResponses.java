package kitchenpos.order.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

import kitchenpos.order.domain.Order;

public class OrderResponses {

    private final List<OrderResponse> orderResponses;

    private OrderResponses(List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrderResponses from(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::from)
            .collect(collectingAndThen(toList(), OrderResponses::new));
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
