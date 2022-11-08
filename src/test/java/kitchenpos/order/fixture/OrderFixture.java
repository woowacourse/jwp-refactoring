package kitchenpos.order.fixture;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixture {

    public static Order createOrder(OrderStatus orderStatus) {
        return Order.builder()
                .orderStatus(orderStatus)
                .build();
    }

    public static Order createOrder(Long orderTableId, OrderStatus orderStatus) {
        return Order.builder()
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .build();
    }

    public static Order createOrder(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return Order.builder()
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .orderedTime(orderedTime)
                .build();
    }
}
