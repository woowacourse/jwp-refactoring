package kitchenpos.fixture;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;

public class OrderFixture {

    public static Order ORDER_FOR(final Long tableId, final String status) {
        final Order order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(status);
        return order;
    }
}
