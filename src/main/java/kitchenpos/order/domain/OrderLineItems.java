package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public void add(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
