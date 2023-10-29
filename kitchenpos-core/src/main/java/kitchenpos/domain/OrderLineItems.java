package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    private OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems of(final List<OrderLineItem> orderLineItems, final int menuIdSize) {
        validateEmpty(orderLineItems);
        validateMenuSize(orderLineItems, menuIdSize);
        return new OrderLineItems(orderLineItems);
    }

    private static void validateEmpty(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateMenuSize(final List<OrderLineItem> orderLineItems, final int menuIdSize) {
        if (orderLineItems.size() != menuIdSize) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
