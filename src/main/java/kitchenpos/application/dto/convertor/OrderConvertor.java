package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderConvertor {

    private OrderConvertor() {
    }

    public static Order convertToOrder(final OrderRequest request) {
        return new Order(request.getOrderTableId(), convertToOrderLineItem(request.getOrderLineItems()));
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

    public static List<OrderLineItem> convertToOrderLineItem(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderConvertor::convertToOrderLineItem)
            .collect(Collectors.toUnmodifiableList());
    }

    public static OrderLineItem convertToOrderLineItem(final OrderLineItemRequest request) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(request.getMenuId());
        orderLineItem.setQuantity(request.getQuantity());
        return orderLineItem;
    }
}
