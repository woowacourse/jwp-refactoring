package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @JoinColumn(name = "menu_id")
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long id, final Long menuId, final long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final Long menuId, final long quantity) {
        return new OrderLineItem(null, menuId, quantity);
    }


    public Long getId() {
        return id;
    }
}
