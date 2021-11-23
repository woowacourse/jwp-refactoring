package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
        this(new ArrayList<>());
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void addAll(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public boolean isEmpty() {
        return orderLineItems.isEmpty();
    }

    public List<Long> collectMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean hasSameSize(int size) {
        return orderLineItems.size() == size;
    }

    public List<OrderLineItem> toList() {
        return orderLineItems;
    }
}
