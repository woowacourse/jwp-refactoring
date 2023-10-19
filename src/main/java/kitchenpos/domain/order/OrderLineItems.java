package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany
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
