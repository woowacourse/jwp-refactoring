package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders"))
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void addAll(final List<OrderLineItem> requestOrderLineItems) {
        orderLineItems.addAll(requestOrderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
