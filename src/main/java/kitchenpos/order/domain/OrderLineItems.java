package kitchenpos.order.domain;

import kitchenpos.exception.InvalidOrderLineItemsToOrder;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "order_id", nullable = false)
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

    public List<OrderLineItem> getValues() {
        return values;
    }
}
