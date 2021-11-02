package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        check(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public void check(List<OrderLineItem> value) {
        if (CollectionUtils.isEmpty(value)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public void checkSize(Long value) {
        if (orderLineItems.size() != value) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(orderLineItems);
    }

    public void updateOrderId(Long orderId) {
        orderLineItems.forEach(it -> it.setOrderId(orderId));
    }
}
