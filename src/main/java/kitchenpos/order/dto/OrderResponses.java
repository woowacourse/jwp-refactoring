package kitchenpos.order.dto;

import java.util.List;

public class OrderResponses {

    private final List<OrderResponse> orderResponses;

    private OrderResponses(List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrderResponses from(List<OrderResponse> orderResponses) {
        return new OrderResponses(orderResponses);
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
