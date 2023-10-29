package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems create() {
        return new OrderLineItems(new ArrayList<>());
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
