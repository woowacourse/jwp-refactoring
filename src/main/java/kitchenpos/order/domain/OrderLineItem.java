package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long id, final String name, final BigDecimal price, final long quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final String name, final BigDecimal price, final long quantity) {
        return new OrderLineItem(null, name, price, quantity);
    }


    public Long getId() {
        return id;
    }
}
