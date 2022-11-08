package kitchenpos.domain.order;

import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(value = AccessType.FIELD)
public class OrderMenuItem {
    @Column(name = "menu_name", nullable = false)
    private String name;

    @Column(name = "menu_price", nullable = false)
    private BigDecimal price;

    protected OrderMenuItem() {
    }

    public OrderMenuItem(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
