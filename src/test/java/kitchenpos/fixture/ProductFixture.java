package kitchenpos.fixture;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private static final String PRODUCT_NAME = "후라이드 치킨";

    public static Product createProduct(Long id, Price price) {
        return new Product(id, PRODUCT_NAME, price);
    }

    public static Product createProductWithId(Long id) {
        return createProduct(id, new Price(BigDecimal.valueOf(19000L)));
    }

    public static Product createProductWithPrice(BigDecimal price) {
        return createProduct(null, new Price(price));
    }

    public static Product createProductWithoutId() {
        return createProduct(null, new Price(BigDecimal.ONE));
    }

}
