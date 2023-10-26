package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private String name;

    @Column(name = "price", columnDefinition = "bigint")
    private BigDecimal price;
    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(final String name, final BigDecimal price, final long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    public long getQuantity() {
        return quantity;
    }
}
