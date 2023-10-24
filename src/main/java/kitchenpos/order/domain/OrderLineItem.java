package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private Long menuId;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, long quantity, Long menuId) {
        this.seq = seq;
        this.quantity = quantity;
        this.menuId = menuId;
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
}
