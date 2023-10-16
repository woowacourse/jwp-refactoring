package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;

public class OrderLineItems {

    private final List<OrderLineItem> collection;

    public OrderLineItems(List<OrderLineItem> collection) {
        this.collection = collection;
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    public List<OrderLineItem> getCollection() {
        return new ArrayList<>(collection);
    }
}
