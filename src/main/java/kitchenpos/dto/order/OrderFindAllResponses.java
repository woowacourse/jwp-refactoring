package kitchenpos.dto.order;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderFindAllResponses {
    private List<OrderFindAllResponse> orderFindAllResponses;

    protected OrderFindAllResponses() {
    }

    public OrderFindAllResponses(List<OrderFindAllResponse> orderFindAllResponses) {
        this.orderFindAllResponses = orderFindAllResponses;
    }

    public static OrderFindAllResponses from(List<Order> orders) {
        return orders.stream()
                .map(OrderFindAllResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), OrderFindAllResponses::new));
    }

    public List<OrderFindAllResponse> getOrderFindAllResponses() {
        return orderFindAllResponses;
    }
}
