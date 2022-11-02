package kitchenpos.fixture.domain;

import java.time.LocalDateTime;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

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
