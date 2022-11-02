package kitchenpos.support.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

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
