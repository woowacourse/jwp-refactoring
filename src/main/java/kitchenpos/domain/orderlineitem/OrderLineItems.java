package kitchenpos.domain.orderlineitem;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.exception.InvalidStateException;

public class OrderLineItems {

    final List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
        orderLineItems = new ArrayList<>();
    }

    public void validateNotEmpty() {
        if (orderLineItems.isEmpty()) {
            throw new InvalidStateException("OrderLineItems가 비어있습니다.");
        }
    }

    public void add(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(orderLineItems);
    }

    public void assignOrder(Order order) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.assignOrder(order);
        }
    }
}
