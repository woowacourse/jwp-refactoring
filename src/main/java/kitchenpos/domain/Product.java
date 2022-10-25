package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.InvalidProductException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

    private static final int PRICE_STANDARD = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        if (isNotAllowedPrice(price)) {
            throw new InvalidProductException();
        }
    }

    private boolean isNotAllowedPrice(final BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < PRICE_STANDARD;
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product product)) {
            return false;
        }
        return Objects.equals(id, product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
