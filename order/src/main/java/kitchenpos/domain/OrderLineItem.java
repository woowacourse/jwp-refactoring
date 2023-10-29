package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;
    private String name;
    private Price price;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Long menuId, final String name, final Price price, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long menuId, final String name, final Price price, final long quantity) {
        this(null, menuId, name, price, quantity);
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

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
