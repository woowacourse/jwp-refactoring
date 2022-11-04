package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> values = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> values, final Order order) {
        addAll(values, order);
        this.values = values;
    }

    public List<OrderLineItem> getValues() {
        return values;
    }

    public void addAll(final List<OrderLineItem> orderLineItems, final Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.changeOrder(order));
        values.addAll(orderLineItems);
    }
}
