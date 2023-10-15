package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.ProductPriceIsNegativeException;
import kitchenpos.exception.ProductPriceIsNotProvidedException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        validatePriceIsNonNull(price);
        validatePriceIsNotNegative(price);
        this.name = name;
        this.price = price;
    }

    private void validatePriceIsNonNull(BigDecimal price) {
        if (price == null) {
            throw new ProductPriceIsNotProvidedException();
        }
    }

    private void validatePriceIsNotNegative(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductPriceIsNegativeException();
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
