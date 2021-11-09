package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@AttributeOverride(
        name = "id", column = @Column(name = "seq")
)
public class OrderLineItem extends BaseEntity {

    @ManyToOne
    private Order order;

    @ManyToOne
    private Menu menu;

    private long quantity;

    public OrderLineItem() {}

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        super(seq);
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
