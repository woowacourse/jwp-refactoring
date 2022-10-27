package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price", scale = 2)
    private BigDecimal price;

    protected Product() {
    }

    public Product(final String name, final BigDecimal price) {
        validateNotNull(price);
        validateNotNegative(price);
        this.name = name;
        this.price = price;
    }

    private void validateNotNull(final BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNotNegative(final BigDecimal decimal) {
        if (decimal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
