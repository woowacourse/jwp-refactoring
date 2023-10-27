package kitchenpos.order.domain;

import kitchenpos.common.BaseDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;
    private String name;
    private BigDecimal price;
    private Long menuId;
    private long quantity;

    public OrderLineItem(final Long seq, final String name, final BigDecimal price, final Long menuId, final long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final String name, final BigDecimal price, final Long menuId, final long quantity) {
        this(null, name, price, menuId, quantity);
    }

    protected OrderLineItem() {
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
