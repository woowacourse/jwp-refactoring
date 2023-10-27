package kitchenpos.order.domain;

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
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    protected OrderLineItems() {
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
