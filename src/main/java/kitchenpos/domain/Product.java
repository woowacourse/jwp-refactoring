package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Embedded
    private ProductName name;
    @NotNull
    @Embedded
    private ProductPrice price;

    public Product() {
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = new ProductName(name);
        this.price = new ProductPrice(price);
    }

    public Product(final ProductName name, final ProductPrice price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
