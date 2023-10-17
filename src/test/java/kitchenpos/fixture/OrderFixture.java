package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static Order create(OrderStatus orderStatus, Long orderTableId) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }


}
