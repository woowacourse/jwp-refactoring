package kitchenpos.support;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public enum OrderFixture {

    ORDER_COOKING_1(COOKING.name(), LocalDateTime.now()),
    ;

    private final String orderStatus;
    private final LocalDateTime orderedTime;

    OrderFixture(String orderStatus, LocalDateTime orderedTime) {
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order 생성(final long orderTableId) {
        return new Order(orderTableId, this.orderStatus, this.orderedTime);
    }

    public Order 생성(final long orderTableId, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, this.orderStatus, this.orderedTime, orderLineItems);
    }

    public Order 생성(final long orderTableId, final LocalDateTime localDateTime) {
        return new Order(orderTableId, this.orderStatus, localDateTime);
    }
}
