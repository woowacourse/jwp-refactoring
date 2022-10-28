package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createDefaultWithoutId() {
        return new Product(null, "name", BigDecimal.valueOf(10000L));
    }

    public static Product createWithPrice(final Long priceValue) {
        BigDecimal price = getPrice(priceValue);
        return new Product(null, "name", price);
    }

    private static BigDecimal getPrice(final Long priceValue) {
        BigDecimal price = null;
        if (priceValue != null) {
            price = BigDecimal.valueOf(priceValue);
        }
        return price;
    }

    public static Product createWithIdAndPrice(final Long id, final Long priceValue) {
        final BigDecimal price = getPrice(priceValue);
        return new Product(id, "name", price);
    }

    public static Product requestCreate(final int port) {
        return RestTemplateFixture.create()
                .postForEntity("http://localhost:" + port + "/api/products",
                        createDefaultWithoutId(), Product.class)
                .getBody();
    }
}
