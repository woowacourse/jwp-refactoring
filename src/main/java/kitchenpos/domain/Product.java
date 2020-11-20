package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.exception.InvalidProductPriceException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    public Product() {
    }

    public Product(Long id, String name, BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new InvalidProductPriceException();
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException(price);
        }

        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(null, name, price);
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

    public BigDecimal calculatePrice(int quantity) {
        return this.price.multiply(BigDecimal.valueOf(quantity));
    }
}
