package kitchenpos.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> elements;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> elements) {
        this.elements = elements;
    }

    public List<OrderLineItem> getElements() {
        return elements;
    }

    public void setOrder(final Order order) {
        for (OrderLineItem element : elements) {
            element.setOrder(order);
        }
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
