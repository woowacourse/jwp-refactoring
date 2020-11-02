package kitchenpos.domain.product;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import kitchenpos.domain.base.BaseIdEntity;

@Entity
public class Product extends BaseIdEntity {

    @Embedded
    private ProductName name;

    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    private Product(Long id, ProductName name, ProductPrice price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, new ProductName(name), new ProductPrice(price));
    }

    public static Product entityOf(String name, BigDecimal price) {
        return of(null, name, price);
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + name + '\'' +
            ", price=" + price +
            '}';
    }
}
