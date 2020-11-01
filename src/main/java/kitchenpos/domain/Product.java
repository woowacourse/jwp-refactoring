package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
@Entity
public class Product extends BaseEntity {
    private String name;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}
