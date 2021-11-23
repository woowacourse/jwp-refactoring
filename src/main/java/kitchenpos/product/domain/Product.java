package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.generic.Money;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Money price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this(null, name, new Money(price));
    }

    private Product(Long id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public Money calculatePrice(long times) {
        return price.times(times);
    }
}
