package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private OrderLineItemQuantity quantity;

    @Embedded
    private OrderLineItemName name;

    @Embedded
    private OrderLineItemPrice price;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long quantity, String name, BigDecimal price) {
        this(null, quantity, name, price);
    }

    public OrderLineItem(Long seq, Long quantity, String name, BigDecimal price) {
        this.seq = seq;
        this.quantity = new OrderLineItemQuantity(quantity);
        this.name = new OrderLineItemName(name);
        this.price = new OrderLineItemPrice(price);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
