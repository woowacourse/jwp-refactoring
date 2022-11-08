package kitchenpos.order;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderPrice {
    @Column(name = "price")
    private BigDecimal value;

    protected OrderPrice() {
    }

    public OrderPrice(final BigDecimal value) {
        this.value = value;
    }

    public OrderPrice(final long value) {
        this.value = BigDecimal.valueOf(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(final BigDecimal price) {
        this.value = price;
    }
}
