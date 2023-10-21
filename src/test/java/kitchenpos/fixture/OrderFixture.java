package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

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
