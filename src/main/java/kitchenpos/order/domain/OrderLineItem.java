package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;
    private String name;
    private BigDecimal price;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final String name, final BigDecimal price, final long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final String name, final BigDecimal price, final long quantity) {
        validate(name, price, quantity);
        return new OrderLineItem(null, name, price, quantity);
    }

    private static void validate(final String name, final BigDecimal price, final long quantity) {
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
    }

    private static void validateName(final String name) {
        if (name.isEmpty() || name.length() > 64) {
            throw new IllegalArgumentException();
        }
    }

    private static void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateQuantity(final Long quantity) {
        if (Objects.isNull(quantity) || quantity < 0) {
            throw new IllegalArgumentException();
        }
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
