package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private long quantity;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getOrderId() {
        return order.getId();
    }
}
