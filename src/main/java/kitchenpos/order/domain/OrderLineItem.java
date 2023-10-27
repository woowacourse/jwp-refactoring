package kitchenpos.order.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.Price;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String menuName;

    private Price menuPrice;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Order order, final String menuName, final Price menuPrice, final long quantity) {
        this(null, order, menuName, menuPrice, quantity);
    }

    public OrderLineItem(final Long seq, final Order order, final String menuName, final Price menuPrice,
                         final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
