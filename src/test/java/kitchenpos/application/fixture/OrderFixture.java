package kitchenpos.application.fixture;

import java.time.LocalDateTime;

import kitchenpos.domain.Order;

public class OrderFixture {

    public static Order createWithStatus(Long orderTableId, String status) {
        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(status);
        return order;
    }

    public static Order createEmptyOrderLines() {
        return new Order();
    }
}
