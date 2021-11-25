package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> values;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> values) {
        this.values = values;
    }

    public boolean isDifferentSize(long size) {
        return this.values.size() != size;
    }

    public void connectOrder(Order order) {
        for (OrderLineItem orderLineItem : values) {
            orderLineItem.connectOrder(order);
        }
    }

    public List<Long> getMenuIds() {
        return values.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
