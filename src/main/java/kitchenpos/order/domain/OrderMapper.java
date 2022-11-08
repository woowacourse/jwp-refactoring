package kitchenpos.order.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order mapOrder(Order entity, List<OrderLineItem> orderLineItems) {
        Order order = new Order(entity.getId(),
                entity.getOrderTableId(),
                entity.getOrderStatus(),
                entity.getOrderedTime(),
                new OrderLineItems(orderLineItems));
        order.placeOrderId();
        return order;
    }
}
