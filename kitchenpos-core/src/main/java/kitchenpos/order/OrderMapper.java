package kitchenpos.order;

import kitchenpos.order.request.OrderCreateRequest;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order mapToOrder(OrderCreateRequest request) {
        return Order.of(
                request.getOrderTableId()
        );
    }
}
