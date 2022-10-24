package kitchenpos.support.fixture.domain;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;

public enum OrderFixture {

    COOKING,
    MEAL,
    COMPLETION,
    ;

    public Order getOrder(Long orderTableId) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(this.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    public Order getOrder(Long id, Long orderTableId) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(this.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }
}
