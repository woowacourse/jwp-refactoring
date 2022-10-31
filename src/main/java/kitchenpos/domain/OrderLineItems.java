package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.exception.InvalidOrderLineItemsException;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> values = new ArrayList<>();

    public OrderLineItems(final List<OrderLineItem> values) {
        validateEmptyItems(values);
        this.values.addAll(values);
    }

    private void validateEmptyItems(final List<OrderLineItem> values) {
        if (values.isEmpty()) {
            throw new InvalidOrderLineItemsException("세부 주문 내역이 비어있습니다.");
        }
    }

    protected OrderLineItems() {
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
