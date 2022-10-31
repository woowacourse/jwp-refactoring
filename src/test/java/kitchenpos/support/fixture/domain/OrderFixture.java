package kitchenpos.support.fixture.domain;

import kitchenpos.order.domain.Order;

public enum OrderFixture {

    COOKING,
    MEAL,
    COMPLETION,
    ;

    public Order getOrder(Long orderTableId) {
        return Order.from(orderTableId);
    }

    public Order getOrder(Long id, Long orderTableId) {
        Order order = Order.from(orderTableId);
        return new Order(id, orderTableId, order.getOrderStatus(), order.getOrderedTime());
    }
}
