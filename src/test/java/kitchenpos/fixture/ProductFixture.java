package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createDefaultWithoutId() {
        final BigDecimal priceValue = BigDecimal.valueOf(10000L);
        return new Product( "name", new Price(priceValue));
    }

    public static Product createWithPrice(final Long priceValue) {
        Price price = getPrice(priceValue);
        return new Product( "name", price);
    }

    private static Price getPrice(final Long priceValue) {
        BigDecimal priceDecimalValue = null;
        if (priceValue != null) {
            priceDecimalValue = BigDecimal.valueOf(priceValue);
        }
        return new Price(priceDecimalValue);
    }
}
