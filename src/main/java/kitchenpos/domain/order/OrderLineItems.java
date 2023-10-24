package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.InvalidOrderLineItemException;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderLineItem> values = new ArrayList<>();

    protected OrderLineItems() {
    }

    private OrderLineItems(final List<OrderLineItem> values) {
        this.values = values;
    }

    public static OrderLineItems of(final Order order, final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        initOrderLineItem(order, orderLineItems);

        return new OrderLineItems(orderLineItems);
    }

    private static void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new InvalidOrderLineItemException();
        }
    }

    private static void initOrderLineItem(final Order order, final List<OrderLineItem> orderLineItems) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.initOrder(order);
        }
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
