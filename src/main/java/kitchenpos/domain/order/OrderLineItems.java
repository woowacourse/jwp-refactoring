package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public void add(final OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
