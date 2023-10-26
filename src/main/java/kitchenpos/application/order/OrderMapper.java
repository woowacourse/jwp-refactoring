package kitchenpos.application.order;

import kitchenpos.application.order.request.OrderCreateRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderValidator;

public class OrderMapper {

    public static Order mapToOrder(OrderCreateRequest request, OrderValidator orderValidator) {
        return Order.of(
                request.getOrderTableId(),
                orderValidator,
                request.getOrderLineItems()
        );
    }
}
