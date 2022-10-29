package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> values = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> values) {
        this.values = values;
    }

    public void setOrder(Order order) {
        for (OrderLineItem orderLineItem : values) {
            orderLineItem.setOrder(order);
        }
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
