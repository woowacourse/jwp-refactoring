package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @JoinColumn(updatable = false, nullable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
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
