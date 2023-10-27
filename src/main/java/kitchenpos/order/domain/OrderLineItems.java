package kitchenpos.order.domain;

import kitchenpos.exception.InvalidOrderLineItemsToOrder;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderLineItem> values = new ArrayList<>();

    public OrderLineItems() {}

    public OrderLineItems(final List<OrderLineItem> values) {
        this.values = values;
    }

    public void validateOrderLineItems() {
        if (CollectionUtils.isEmpty(values)) {
            throw new InvalidOrderLineItemsToOrder("주문 항목이 없습니다.");
        }
    }

    public void addOrderLineItems(final Order order) {
        for (OrderLineItem orderLineItem : values) {
            orderLineItem.updateOrder(order);
        }
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
