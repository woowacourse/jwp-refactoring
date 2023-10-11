package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order 주문(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        return order;
    }
}
