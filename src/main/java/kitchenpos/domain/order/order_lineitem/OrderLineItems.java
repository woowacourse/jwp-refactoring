package kitchenpos.domain.order.order_lineitem;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.exception.OrderException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private final List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
        orderLineItems = null;
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = List.copyOf(orderLineItems);
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems) || CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderException.NoOrderLineItemsException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
