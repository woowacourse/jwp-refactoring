package kitchenpos.domain.order;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderLineItems);
    }

    public boolean hasDuplicate() {
        return orderLineItems.size() != orderLineItems.stream()
                .map(OrderLineItem::getName)
                .collect(Collectors.toSet())
                .size();
    }

    public OrderLineItems arrangeOrder(final Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.arrangeOrder(order));
        return this;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
