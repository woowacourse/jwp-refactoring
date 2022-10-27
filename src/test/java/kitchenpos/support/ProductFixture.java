package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

    public static Product 상품 = 상품_생성("상품", BigDecimal.valueOf(10000));

    public static Product 상품_생성(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
