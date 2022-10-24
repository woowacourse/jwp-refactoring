package kitchenpos.support;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;

public enum OrderFixture {

    ORDER_1("COOKING", LocalDateTime.now());

    private final String orderStatus;
    private final LocalDateTime orderedTime;

    OrderFixture(String orderStatus, LocalDateTime orderedTime) {
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order 생성(final long orderTableId) {
        return new Order(orderTableId, this.orderStatus, this.orderedTime);
    }

    public Order 생성(final long orderTableId, final LocalDateTime localDateTime) {
        return new Order(orderTableId, this.orderStatus, localDateTime);
    }
}
