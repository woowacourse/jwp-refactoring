package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    protected Product() {
    }

    private Product(
            final Long id,
            final String name,
            final BigDecimal price
    ) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name,
                             final Long price) {
        if (price == null) {
            throw new IllegalArgumentException("상품의 가격은 null일 수 없습니다.");
        }
        return new Product(null, name, BigDecimal.valueOf(price));
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품의 가격은 null이거나 음수일 수 없습니다.");
        }
    }

    public BigDecimal multiplyPrice(final Long quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("수량은 null일 수 없습니다.");
        }
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
