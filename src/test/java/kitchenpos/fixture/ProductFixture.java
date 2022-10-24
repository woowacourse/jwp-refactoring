package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 상품_생성(final String name, final int price) {
        return 상품_생성(name, BigDecimal.valueOf(price));
    }

    public static Product 상품_생성(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
