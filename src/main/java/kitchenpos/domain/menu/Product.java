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
    private Price price;

    protected Product() {
    }

    public Product(String name, Long price) {
        this.name = name;
        this.price = new Price(price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
