package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

@Getter
public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        if (value == null || value.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public static Price ofMenu(BigDecimal value, List<ProductQuantity> menuProductQuantities) {
        BigDecimal individualPriceSum = calculateIndividualPriceSum(menuProductQuantities);
        if (value == null || value.compareTo(individualPriceSum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 구성품을 개별적으로 구매했을 때에 비해 비싸면 안됩니다.");
        }
        return new Price(value);
    }

    private static BigDecimal calculateIndividualPriceSum(List<ProductQuantity> menuProductQuantities) {
        return menuProductQuantities.stream()
            .map(ProductQuantity::calculateTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Price multiply(int operand) {
        return new Price(value.multiply(BigDecimal.valueOf(operand)));
    }
}
