package kitchenpos.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static kitchenpos.exception.OrderExceptionType.ORDER_LINE_ITEMS_CAN_NOT_EMPTY;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.OrderException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = ALL, fetch = EAGER)
    private List<OrderLineItem> items;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> items) {
        validate(items);
        this.items = items;
    }

    private void validate(List<OrderLineItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            throw new OrderException(ORDER_LINE_ITEMS_CAN_NOT_EMPTY);
        }
    }

    public void setOrderToAll(Order order) {
        items.forEach(it -> it.setOrder(order));
    }

    public List<OrderLineItem> items() {
        return items;
    }
}
