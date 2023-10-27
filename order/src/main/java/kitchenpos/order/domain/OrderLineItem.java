package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.vo.MenuSpecification;
import kitchenpos.order.vo.Quantity;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    @Embedded
    private MenuSpecification menuSpecification;

    OrderLineItem(Long seq, Long menuId, Quantity quantity, MenuSpecification menuSpecification) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuSpecification = menuSpecification;
    }

    OrderLineItem(Long menuId, Quantity quantity, MenuSpecification menuSpecification) {
        this(null, menuId, quantity, menuSpecification);
    }

    protected OrderLineItem() {
    }

    public static OrderLineItem of(Long menuId, long quantity, MenuSpecification menuSpecification) {
        return new OrderLineItem(menuId, Quantity.valueOf(quantity), menuSpecification);
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

    public MenuSpecification getMenuSpecification() {
        return menuSpecification;
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }
}
