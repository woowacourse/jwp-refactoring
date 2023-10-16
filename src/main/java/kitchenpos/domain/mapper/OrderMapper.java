package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.List;

public class OrderMapper {
    private OrderMapper() {
    }

    public static Order toOrder(CreateOrderRequest request, List<OrderLineItem> orderLineItems) {
        return Order.builder()
                .orderTableId(request.getOrderTableId())
                .orderStatus(request.getOrderStatus())
                .orderedTime(request.getOrderedTime())
                .orderLineItems(orderLineItems)
                .build();
    }
}
