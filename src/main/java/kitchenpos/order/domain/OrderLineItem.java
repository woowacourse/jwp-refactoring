package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.order.OrderPrice;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Embedded
    private OrderPrice price;
    private String name;
    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final String name, final OrderPrice price, final long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem(final String name, final OrderPrice price, final long quantity) {
        this(null, name, price, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price.getValue();
    }

    public long getQuantity() {
        return quantity;
    }
}
