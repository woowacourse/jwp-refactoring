package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.order.vo.Quantity;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    OrderLineItem(Long seq, Long menuId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    OrderLineItem(Long menuId, Quantity quantity) {
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
