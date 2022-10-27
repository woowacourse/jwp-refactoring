package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductPrice {

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal value;

    protected ProductPrice() {
    }

    public ProductPrice(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 null 혹은 0 미만일 수 없습니다.");
        }
    }

    public BigDecimal multiply(final long quantity) {
        return value.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductPrice productPrice = (ProductPrice) o;
        return Objects.equals(value, productPrice.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
