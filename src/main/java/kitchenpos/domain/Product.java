package kitchenpos.domain;

import static kitchenpos.domain.exception.ProductExceptionType.PRICE_IS_LOWER_THAN_ZERO;
import static kitchenpos.domain.exception.ProductExceptionType.PRICE_IS_NULL;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.exception.ProductException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null) {
            throw new ProductException(PRICE_IS_NULL);
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException(PRICE_IS_LOWER_THAN_ZERO);
        }
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    protected Product() {

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
