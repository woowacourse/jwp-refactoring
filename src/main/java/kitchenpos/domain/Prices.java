package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Prices {

    private final List<Price> prices;

    public Prices(List<Price> prices) {
        this.prices = prices;
    }

    public Price sumAll() {
        BigDecimal value = prices.stream()
            .map(Price::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Price(value);
    }
}
