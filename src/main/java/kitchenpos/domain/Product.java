package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.annotation.Id;

public class Product {

    @Id
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static Product of(final String name, final BigDecimal price) {
        return new Product(null, name, price);
    }

    private Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = getVerifiedPrice(price);
    }

    private BigDecimal getVerifiedPrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        return price;
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
