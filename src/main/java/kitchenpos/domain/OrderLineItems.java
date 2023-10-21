package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderLineItems {

    private List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean hasSameSizeWith(Long size) {
        return orderLineItems.size() == size;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
