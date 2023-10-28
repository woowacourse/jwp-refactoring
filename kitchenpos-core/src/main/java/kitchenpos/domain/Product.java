package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Price price;

    private String name;

    protected Product() {
    }

    public Product(final String name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name, final BigDecimal price) {
        return new Product(name, new Price(price));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
