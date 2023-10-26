package kitchenpos.order;

import kitchenpos.order.request.OrderCreateRequest;

public class OrderMapper {

    public static Order mapToOrder(OrderCreateRequest request, OrderValidator orderValidator) {
        return Order.of(
                request.getOrderTableId(),
                orderValidator,
                request.getOrderLineItems()
        );
    }
}
