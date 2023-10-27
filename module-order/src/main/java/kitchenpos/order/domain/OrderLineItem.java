package kitchenpos.order.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private MenuSnapshot menuSnapshot;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final MenuSnapshot menuSnapshot, final Long quantity) {
        this.menuId = menuSnapshot.getMenuId();
        this.quantity = quantity;
        this.menuSnapshot = menuSnapshot;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public MenuSnapshot getMenuSnapshot() {
        return menuSnapshot;
    }
}
