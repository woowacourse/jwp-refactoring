package kitchenpos.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Order order;

    @OneToOne
    @JoinColumn(nullable = false, updatable = false)
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Order order, final Menu menu, final long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
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

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }
}
