package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        validateSize(orderLineItems);
    }

    private void validateSize(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() == 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
