package kitchenpos.product;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import suppoert.domain.BaseEntity;

@Entity
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;

    public Product(final String name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    protected Product() {
    }
}
