package kitchenpos.domain.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {
    @Column(name = "product_price")
    private BigDecimal price;

    public ProductPrice() {
    }

    private ProductPrice(BigDecimal price) {
        this.price = price;
    }

    public static ProductPrice of(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        return new ProductPrice(price);
    }

    public static ProductPrice of(int productPrice) {
        return ProductPrice.of(BigDecimal.valueOf(productPrice));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
