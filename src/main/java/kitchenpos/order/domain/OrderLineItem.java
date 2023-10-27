package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.vo.Money;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long orderId;

    private Long menuId;

    private String menuName;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Money price;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(
            final Long orderId,
            final Long menuId,
            final long quantity,
            final String menuName,
            final Money price
    ) {
        this(null, orderId, menuId, quantity, menuName, price);
    }

    public OrderLineItem(
            final Long seq,
            final Long orderId,
            final Long menuId,
            final long quantity,
            final String menuName,
            final Money price
    ) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }


    public long getQuantity() {
        return quantity;
    }
}
