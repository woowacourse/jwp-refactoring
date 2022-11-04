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

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> values = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
        this.values = values;
    }

    public void setOrder(Order order) {
        for (OrderLineItem orderLineItem : values) {
            orderLineItem.setOrder(order);
        }
    }

    public int size() {
        return values.size();
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
