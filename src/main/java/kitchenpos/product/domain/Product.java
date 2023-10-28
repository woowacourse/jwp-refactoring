package kitchenpos.product.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name, final BigDecimal price) {
        validate(name, price);
        return new Product(null, name, price);
    }

    private static void validate(final String name, final BigDecimal price) {
        validateName(name);
        validatePrice(price);
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

    public BigDecimal calculatePrice(final long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
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
