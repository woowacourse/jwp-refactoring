package kitchenpos.support;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public enum OrderFixture {

    ORDER_COOKING_1(COOKING, LocalDateTime.now()),
    ORDER_COMPLETION_1(COMPLETION, LocalDateTime.now()),
    ;

    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;

    OrderFixture(OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order 생성(final long orderTableId, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, this.orderStatus, this.orderedTime, orderLineItems);
    }

    public Order 주문항목_없이_생성(final long orderTableId) {
        return new Order(orderTableId, this.orderStatus, this.orderedTime);
    }
}
