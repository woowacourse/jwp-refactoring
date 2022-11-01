package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.badrequest.ProductNameInvalidException;
import kitchenpos.exception.badrequest.ProductPriceInvalidException;
import org.springframework.util.StringUtils;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Product() {
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;

        validateName();
        validatePrice();
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    private void validateName() {
        if (!StringUtils.hasText(this.name)) {
            throw new ProductNameInvalidException(this.name);
        }
    }

    private void validatePrice() {
        if (Objects.isNull(this.price) || isLessThanZero(this.price)) {
            throw new ProductPriceInvalidException(this.price);
        }
    }

    private boolean isLessThanZero(final BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
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
