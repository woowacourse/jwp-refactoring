package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    public OrderLineItem(Long seq, Long menuId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, Quantity quantity) {
        this(null, menuId, quantity);
    }

    protected OrderLineItem() {
    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(menuId, Quantity.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }
}
