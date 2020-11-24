package kitchenpos.fixture;

import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private static final String PRODUCT_NAME = "후라이드 치킨";

    public static Product createProduct(Long id, BigDecimal price) {
        return new Product(id, PRODUCT_NAME, new Price(price));
    }

    public static Product createProductWithoutId() {
        return createProduct(null, BigDecimal.ONE);
    }

    public static Product createProductWithId(Long id) {
        return createProduct(id, BigDecimal.valueOf(19000L));
    }

    public static Product createProductWithPrice(BigDecimal price) {
        return createProduct(null, price);
    }

}
