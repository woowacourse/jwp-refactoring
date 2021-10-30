package kitchenpos.domain.price;

import java.util.ArrayList;
import java.util.List;

public class Prices {

    final List<Price> prices;

    public Prices(List<Price> prices) {
        this.prices = new ArrayList<>(prices);
    }

    public Price sum() {
        return new Price(getPricesSumValue());
    }

    private int getPricesSumValue() {
        return prices.stream()
            .mapToInt(Price::getValueAsInt)
            .sum();
    }
}
