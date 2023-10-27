package kitchenpos.product.domain;

import kitchenpos.product.exception.ProductPriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {

    private BigDecimal price;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        validateProductPrice(price);
        this.price = price;
    }

    private void validateProductPrice(BigDecimal price) {
        if (isNull(price) || isNegative(price)) {
            throw new ProductPriceException("상품 가격은 0 이상이어야 합니다.");
        }
    }

    private boolean isNull(BigDecimal price) {
        return Objects.isNull(price);
    }

    private boolean isNegative(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice that = (ProductPrice) o;
        return Objects.equals(getPrice(), that.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrice());
    }
}
