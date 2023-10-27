package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.common.Name;
import kitchenpos.domain.common.Price;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Price price() {
        return price;
    }

    public Name name() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final Product targetProduct = (Product) target;
        return Objects.equals(getId(), targetProduct.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
