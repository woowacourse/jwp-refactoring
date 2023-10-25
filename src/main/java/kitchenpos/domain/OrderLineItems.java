package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> value = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> value) {
        this.value = value;
    }

    public void addOrderLineItem(final OrderLineItem item) {
        value.add(item);
    }

    public List<OrderLineItem> getValue() {
        return value;
    }
}
