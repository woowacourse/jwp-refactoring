package kitchenpos.common.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문(Long orderTableId) {
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(Long orderId, Long orderTableId) {
        return new Order(orderId, orderTableId, OrderStatus.COOKING, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static Order 주문(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, LocalDateTime.MAX, orderLineItems);
    }
}
