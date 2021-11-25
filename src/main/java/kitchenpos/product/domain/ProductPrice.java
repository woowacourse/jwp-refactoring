package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuQuantity;
import kitchenpos.product.exception.InvalidProductPriceException;

@Embeddable
public class ProductPrice {

    @NotNull
    @Column(name = "price")
    private BigDecimal value;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal value) {
        this.value = value;
        validateNull(this.value);
        validateNegative(this.value);
    }

    private void validateNull(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new InvalidProductPriceException("Price는 Null일 수 없습니다.");
        }
    }

    private void validateNegative(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException(
                String.format("%s 는 0보다 작기 때문에 Price가 될 수 없습니다.", value)
            );
        }
    }

    public MenuPrice multiplyQuantity(MenuQuantity menuQuantity) {
        BigDecimal result = value.multiply(menuQuantity.getDecimalValue());

        return new MenuPrice(result);
    }

    public boolean isBiggerThan(ProductPrice price) {
        return value.compareTo(price.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductPrice price = (ProductPrice) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
