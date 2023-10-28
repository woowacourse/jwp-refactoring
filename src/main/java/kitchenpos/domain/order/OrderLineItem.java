package kitchenpos.domain.order;

import kitchenpos.domain.common.Quantity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @NotNull
    private Long menuId;
    @NotNull
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final Long menuId, final Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }


    public static OrderLineItem of(final Long menuId, final Quantity quantity) {
        return new OrderLineItem(null, menuId, quantity);
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
}
