package kitchenpos.domain;

import kitchenpos.common.BaseDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Product extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Product() {
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
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
