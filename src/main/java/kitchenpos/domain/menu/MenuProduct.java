package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    private BigDecimal price;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final String name, final BigDecimal price, final long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal calculateTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
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
