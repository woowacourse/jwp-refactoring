package kitchenpos.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLineItem> collection;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> collection) {
        validateSize(collection);
        this.collection = collection;
    }

    private void validateSize(List<OrderLineItem> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있으면 생성할 수 없다.");
        }
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    public List<OrderLineItem> getCollection() {
        return new ArrayList<>(collection);
    }
}
