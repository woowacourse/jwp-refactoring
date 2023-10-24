package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
