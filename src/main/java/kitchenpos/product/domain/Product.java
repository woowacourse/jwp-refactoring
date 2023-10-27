package kitchenpos.product.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {
    }

    private Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public static Product of(String name, BigDecimal price) {
        Product product = new Product(name, price);
        product.validatePrice();
        return product;
    }

    private void validatePrice() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
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
