package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.generic.Price;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private String menuName;
    @Embedded
    private Price menuPrice;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(
            final String menuName,
            final Price menuPrice,
            final long quantity
    ) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
