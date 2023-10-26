package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    private Product(
            final Long id,
            final String name,
            final Price price
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name,
                             final BigDecimal price) {
        return new Product(null, name, new Price(price));
    }

    public Price multiplyPrice(final Long quantity) {
        return price.multiplyByQuantity(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
