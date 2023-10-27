package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
