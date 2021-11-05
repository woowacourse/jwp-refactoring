package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;

@Entity
public class Product extends BaseEntity {

    private String name;

    private BigDecimal price;

    public Product() {}

    public Product(Long id, String name, BigDecimal price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
