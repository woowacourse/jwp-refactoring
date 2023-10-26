package kitchenpos.domain.order;

import static javax.persistence.CascadeType.PERSIST;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(fetch = FetchType.EAGER, cascade = PERSIST)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLineItem> items;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> items) {
        this.items = items;
    }

    public List<OrderLineItem> getItems() {
        return items;
    }
}
