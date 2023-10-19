package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "order")
    private List<OrderLineItem> values;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> values) {
        this.values = values;
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
