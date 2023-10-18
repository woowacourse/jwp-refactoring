package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany
    private List<OrderLineItem> collection;

    public OrderLineItems() {
    }

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
