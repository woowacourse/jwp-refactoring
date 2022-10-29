package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;

public class OrderConvertor {

    private OrderConvertor() {
    }

    public static OrderResponse toOrderResponse(final Order savedOrder) {
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
            savedOrder.getOrderedTime(), savedOrder.getOrderLineItems());
    }

    public static List<OrderResponse> toOrderResponses(final List<Order> orders) {
        return orders.stream()
            .map(OrderConvertor::toOrderResponse)
            .collect(Collectors.toUnmodifiableList());
    }
}
