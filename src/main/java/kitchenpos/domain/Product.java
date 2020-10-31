package kitchenpos.domain;

import io.micrometer.core.instrument.util.StringUtils;
import kitchenpos.domain.exceptions.EmptyNameException;
import kitchenpos.domain.exceptions.InvalidPriceException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() { }

    public Product(Long id, String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    private void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new EmptyNameException();
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
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

    public BigDecimal getTotalPrice(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
