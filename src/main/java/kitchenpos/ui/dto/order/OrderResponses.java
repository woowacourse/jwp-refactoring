package kitchenpos.ui.dto.order;

import java.util.List;

public class OrderResponses {

    private final List<OrderResponse> orders;

    public OrderResponses(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrderResponses from(List<OrderResponse> orders) {
        return new OrderResponses(orders);
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
