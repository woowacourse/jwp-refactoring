package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.table.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문(OrderTable orderTable) {
        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(OrderStatus orderStatus) {
        return new Order(null, orderStatus, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(orderTable, orderStatus, LocalDateTime.MAX, List.of());
    }

    public static Order 주문(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, LocalDateTime.MAX, orderLineItems);
    }
}
