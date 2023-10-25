package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import kitchenpos.domain.BaseEntity;

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

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    protected Product() {
    }
}
