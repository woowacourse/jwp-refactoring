package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class MenuPrice {

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected MenuPrice() {
    }

    public MenuPrice(final BigDecimal value) {
        this.value = value;
    }

    public MenuPrice(final BigDecimal value, final BigDecimal productTotalPrice) {
        validate(value, productTotalPrice);
        this.value = value;
    }

    private void validate(final BigDecimal value, final BigDecimal productTotalPrice) {
        validatePositive(value);
        validateScaleLessThanMaximum(value);
        validateValueLessThanProductTotalPrice(value, productTotalPrice);
    }

    private void validateValueLessThanProductTotalPrice(final BigDecimal value, final BigDecimal productTotalPrice) {
        if (value.compareTo(productTotalPrice) > 0) {
            throw new InvalidPriceException("가격은 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    private void validatePositive(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("가격은 0원 이상이어야 합니다.");
        }
    }

    private void validateScaleLessThanMaximum(final BigDecimal value) {
        if (value.scale() > 2) {
            throw new InvalidPriceException("가격은 소수 부분은 2자리까지만 입력 가능합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }
}
