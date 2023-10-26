package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
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

    @OneToOne
    @JoinColumn(nullable = false, updatable = false)
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }
}
