package kitchenpos.domain.order;

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
    private Long menuId;
    
    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    /**
     * DB 에 저장되지 않은 객체
     * DB 에 저장하기 위한 객체
     */
    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, menuId, quantity);
    }

    /**
     * DB 에 저장된 객체
     */
    public OrderLineItem(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
