package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createDefaultWithoutId() {
        return new Product(null, "name", BigDecimal.valueOf(10000L));
    }

    public static Product createWithPrice(final Long priceValue) {
        BigDecimal price = null;
        if (priceValue != null) {
            price = BigDecimal.valueOf(priceValue);
        }
        return new Product(null, "name", price);
    }

    public static Product requestCreate(final int port) {
        return RestTemplateFixture.create()
                .postForEntity("http://localhost:" + port + "/api/products",
                        createDefaultWithoutId(), Product.class)
                .getBody();
    }
}
