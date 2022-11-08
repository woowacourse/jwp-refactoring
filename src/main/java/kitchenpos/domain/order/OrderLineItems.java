package kitchenpos.domain.order;

import java.util.List;
import kitchenpos.domain.order.OrderLineItem;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
