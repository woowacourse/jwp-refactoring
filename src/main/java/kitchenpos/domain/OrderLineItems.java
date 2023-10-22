package kitchenpos.domain;

import java.util.List;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class OrderLineItems {

    @MappedCollection(idColumn = "id")
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validate(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
