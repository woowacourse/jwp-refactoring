package kitchenpos.domain.product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    public static Product from(final String name, final BigDecimal price) {
        return new Product(Name.from(name), Price.from(price));
    }

    protected Product(){}

    private Product(final Name name, final Price price) {
        this(null, name, price);
    }

    public Product(final Long id, final Name name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
