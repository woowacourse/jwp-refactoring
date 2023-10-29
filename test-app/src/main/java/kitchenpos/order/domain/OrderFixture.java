package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 조리_상태_주문() {
        return new Order(null, COOKING, LocalDateTime.MAX, List.of());
    }

    public static Order 계산_완료_상태_주문() {
        return new Order(null, COMPLETION, LocalDateTime.MAX, List.of());
    }

    public static Order 조리_상태_주문(Long orderTableId) {
        return new Order(orderTableId, COOKING, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(OrderStatus orderStatus) {
        return new Order(null, orderStatus, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, LocalDateTime.MAX, orderLineItems);
    }
}
