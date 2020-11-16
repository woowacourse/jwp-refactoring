package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;

public class OrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;

    public OrderResponse(Long id, Long orderTableId, String orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name());
    }

    public static List<OrderResponse> listOf(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
