package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.exception.InvalidPriceException;

public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        this.value = value;
        validateNull(this.value);
        validateNegative(this.value);
    }

    private void validateNull(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new InvalidPriceException("Price는 Null일 수 없습니다.");
        }
    }

    private void validateNegative(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException(
                String.format("%s 는 0보다 작기 때문에 Price가 될 수 없습니다.", value)
            );
        }
    }

    public static Price of(List<MenuProduct> menuProducts) {
        BigDecimal value = menuProducts.stream()
            .map(MenuProduct::productTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Price(value);
    }

    public boolean isBiggerThan(Price price) {
        return value.compareTo(price.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
