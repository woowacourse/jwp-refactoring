package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@AttributeOverride(name = "id", column = @Column(name = "id"))
@Table(name = "product")
@Entity
public class Product extends BaseEntity {
    private String name;

    @Embedded
    private Price price;

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public BigDecimal getPriceValue() {
        return price.getPrice();
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
