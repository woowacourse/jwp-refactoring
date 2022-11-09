package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Price price;

    private String name;

    public Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    protected Product() {
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public String getName() {
        return name;
    }
}
