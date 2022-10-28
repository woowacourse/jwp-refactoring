package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.List;

public class Price {

    private BigDecimal value;

    public Price(final BigDecimal value) {
        validatePrice(value.intValue());
        this.value = value;
    }

    public static Price convertToMenuPrice(final BigDecimal menuPrice, final List<BigDecimal> productPrice,
                                           final List<Long> quantities) {

        BigDecimal sumOfIndividualProductPrice = BigDecimal.ZERO;
        for (int i = 0; i < productPrice.size(); i++) {
            sumOfIndividualProductPrice = calculateIndividualProductPrice(productPrice, quantities,
                    sumOfIndividualProductPrice, i);
        }

        if (menuPrice.compareTo(sumOfIndividualProductPrice) > 0) {
            throw new IllegalArgumentException();
        }

        return new Price(menuPrice);
    }

    private static BigDecimal calculateIndividualProductPrice(List<BigDecimal> productPrice, List<Long> quantities,
                                                              BigDecimal sum, int i) {
        return sum.add(productPrice.get(i).multiply(BigDecimal.valueOf(quantities.get(i))));
    }

    private void validatePrice(final Integer price) {
        if (price == null || price.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
