package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;

public class OrderConvertor {

    private OrderConvertor() {
    }

    public static Order convertToOrder(final OrderChangeRequest request) {
        final Order order = new Order();
        order.setOrderStatus(request.getOrderStatus());
        return order;
    }

    public static OrderResponse convertToOrderResponse(final Order savedOrder) {
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
            savedOrder.getOrderedTime(), savedOrder.getOrderLineItems());
    }

    public static List<OrderResponse> convertToOrderResponse(final List<Order> orders) {
        return orders.stream()
            .map(OrderConvertor::convertToOrderResponse)
            .collect(Collectors.toUnmodifiableList());
    }
}
