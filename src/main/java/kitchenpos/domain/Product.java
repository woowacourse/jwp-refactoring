package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Money price;

    public Product(Long id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, Money price) {
        this(null, name, price);
    }

    protected Product() {
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, Money.valueOf(price));
    }

    public BigDecimal calculateTotalPrice(Quantity quantity) {
        return price.multiply(quantity.getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPriceValue() {
        return price.getValue();
    }

    public Money getPrice() {
        return price;
    }
}
