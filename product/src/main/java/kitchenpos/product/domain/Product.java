package kitchenpos.product.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.product.vo.Price;

@Entity
public class Product {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    protected Product() {
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, Price.valueOf(price));
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

    public Price getPrice() {
        return price;
    }
}
