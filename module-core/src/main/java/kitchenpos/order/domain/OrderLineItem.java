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

    @Column(name = "menu_id", nullable = false)
    private Long menuHistoryId;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuHistoryId, final long quantity) {
        this.menuHistoryId = menuHistoryId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuHistoryId() {
        return menuHistoryId;
    }

    public long getQuantity() {
        return quantity;
    }
}
