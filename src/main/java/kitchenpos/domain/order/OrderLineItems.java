package kitchenpos.domain.order;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order" ,fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems create(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public boolean isEmpty() {
        return orderLineItems.isEmpty();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
