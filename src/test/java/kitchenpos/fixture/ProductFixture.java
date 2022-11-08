package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.core.product.domain.Product;

public class ProductFixture {

    public static Product getProductRequest() {
        return getProductRequest(10000);
    }

    public static Product getProductRequest(final int price) {
        return getProductRequest(BigDecimal.valueOf(price));
    }

    public static Product getProductRequest(final BigDecimal price) {
        return new Product("후라이드", price);
    }

    public static Product getProduct(final Long id, final Integer price) {
        return getProduct(id, "후라이드", price);
    }

    public static Product getProduct() {
        return getProduct(1L, "후라이드", 10000);
    }

    public static Product getProduct(final Long id, final String name, final Integer price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }
}
