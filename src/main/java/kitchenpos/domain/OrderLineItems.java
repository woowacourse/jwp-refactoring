package kitchenpos.domain;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        this.orderLineItems = orderLineItems;
    }

    public void validateMenuSize(Long menuSize) {
        if (orderLineItems.size() != menuSize) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
