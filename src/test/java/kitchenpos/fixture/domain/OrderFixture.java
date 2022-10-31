package kitchenpos.fixture.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order createOrder(Long id) {
        return Order.builder()
                .id(id)
                .build();
    }

    public static Order createOrder(String orderStatus) {
        return Order.builder()
                .orderStatus(orderStatus)
                .build();
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.builder()
                .orderTableId(orderTableId)
                .orderLineItems(orderLineItems)
                .build();
    }

    public static Order createOrder(Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.builder()
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .orderLineItems(orderLineItems)
                .build();
    }

    public static Order createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                                    List<OrderLineItem> orderLineItems) {
        return Order.builder()
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .orderedTime(orderedTime)
                .orderLineItems(orderLineItems)
                .build();
    }
}
