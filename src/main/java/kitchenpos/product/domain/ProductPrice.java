package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {

    @Column(name = "price", nullable = false, scale = 2)
    private BigDecimal value;

    public ProductPrice(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    protected ProductPrice() {
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public static ProductPrice from(int value) {
        return new ProductPrice(BigDecimal.valueOf(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductPrice)) {
            return false;
        }
        ProductPrice menuPrice = (ProductPrice) o;
        return Objects.equals(value, menuPrice.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isGreaterThan(ProductPrice menuPrice) {
        return value.compareTo(menuPrice.value) > 0;
    }
}
