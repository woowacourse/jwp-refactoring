package kitchenpos.order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    private Order order;
    @Embedded
    private MenuInfo menuInfo;
    private long quantity;

    public OrderLineItem(final Long seq, final Order order, final MenuInfo menuInfo, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuInfo = menuInfo;
        this.quantity = quantity;
    }

    public static OrderLineItem ofUnsaved(final Order order, final MenuInfo menuInfo, final long quantity) {
        return new OrderLineItem(null, order, menuInfo, quantity);
    }

    public void order(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public MenuInfo getMenuInfo() {
        return menuInfo;
    }

    public Long getMenuId() {
        return menuInfo.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    protected OrderLineItem() {
    }
}
