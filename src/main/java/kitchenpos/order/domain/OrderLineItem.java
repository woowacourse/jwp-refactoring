package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Embedded
    private MenuInfo menuInfo;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order order, final Long menuId, final long quantity,
                         final MenuInfo menuInfo) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuInfo = menuInfo;
    }

    public OrderLineItem(final Long menuId, final long quantity, final MenuInfo menuInfo) {
        this(null, null, menuId, quantity, menuInfo);
    }

    public void changeOrder(final Order order) {
        this.order = order;
    }

    public void updateMenuInfo(final MenuInfo menuInfo) {
        this.menuInfo = menuInfo;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuInfo getMenuInfo() {
        return menuInfo;
    }
}
