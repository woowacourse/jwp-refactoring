package kitchenpos.domain.menu;

import kitchenpos.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
@Entity
public class Product extends BaseEntity {
    private String name;
    private BigDecimal price;

    public Product() {

    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
