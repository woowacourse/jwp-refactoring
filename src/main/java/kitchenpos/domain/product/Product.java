package kitchenpos.domain.product;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.product.vo.Price;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this.name = name;
        this.price = new Price(price);
    }

    public BigDecimal calculateTotalPrice(final long targetQuantity) {
        return price.multiply(BigDecimal.valueOf(targetQuantity));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
