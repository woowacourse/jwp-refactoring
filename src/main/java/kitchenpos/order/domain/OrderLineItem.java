package kitchenpos.order.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String menuName;

    @Embedded
    private Price price;

    private Long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final String menuName, final Price price, final long quantity) {
        this(null, menuName, price, quantity);
    }

    public OrderLineItem(final Long seq, final String menuName, final Price price, final long quantity) {
        this.seq = seq;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
