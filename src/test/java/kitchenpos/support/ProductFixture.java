package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;

public class ProductFixture {

    public static Product createProduct(final String name) {
        return new Product(name, BigDecimal.valueOf(20_000));
    }

    public static Product createProduct(final int price) {
        return new Product("간장 치킨", BigDecimal.valueOf(price));
    }

    public static ProductRequest createProductRequest(final int price) {
        return new ProductRequest("간장 치킨", BigDecimal.valueOf(price));
    }
}
