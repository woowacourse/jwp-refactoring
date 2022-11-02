package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createDefaultWithoutId() {
        return new Product( "name", BigDecimal.valueOf(10000L));
    }

    public static Product createWithPrice(final Long priceValue) {
        BigDecimal price = getPrice(priceValue);
        return new Product( "name", price);
    }

    private static BigDecimal getPrice(final Long priceValue) {
        BigDecimal price = null;
        if (priceValue != null) {
            price = BigDecimal.valueOf(priceValue);
        }
        return price;
    }
}
