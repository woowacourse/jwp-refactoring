package kitchenpos.fixture.domain;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;

public class OrderFixture {

    public static Order createOrder(String orderStatus) {
        return Order.builder()
                .orderStatus(orderStatus)
                .build();
    }

    public static Order createOrder(Long orderTableId, String orderStatus) {
        return Order.builder()
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .build();
    }

    public static Order createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        return Order.builder()
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .orderedTime(orderedTime)
                .build();
    }
}
