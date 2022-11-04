package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.vo.ProductPrice;
import kitchenpos.exception.badrequest.PriceInvalidException;
import kitchenpos.exception.badrequest.ProductNameInvalidException;
import org.springframework.util.StringUtils;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    public Product(final Long id, final String name, final ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;

        validateName();
        validatePrice();
    }

    public Product(final String name, final ProductPrice price) {
        this(null, name, price);
    }

    private void validateName() {
        if (!StringUtils.hasText(this.name)) {
            throw new ProductNameInvalidException(this.name);
        }
    }

    private void validatePrice() {
        if (Objects.isNull(price) || price.isLessThanZero()) {
            throw new PriceInvalidException(price.getValue());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return price;
    }
}
