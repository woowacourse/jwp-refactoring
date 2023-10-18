package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.ProductExceptionType.PRICE_IS_NULL_OR_MINUS_EXCEPTION;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.exception.ProductException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException(PRICE_IS_NULL_OR_MINUS_EXCEPTION);
        }
    }

    public BigDecimal calculateAmount(long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }
}
