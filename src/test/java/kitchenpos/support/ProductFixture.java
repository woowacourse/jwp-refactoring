package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 상품1 = 상품_생성("상품1", BigDecimal.valueOf(10000));
    public static Product 상품2 = 상품_생성("상품2", BigDecimal.valueOf(20000));
    public static Product 상품3 = 상품_생성("상품3", BigDecimal.valueOf(30000));

    public static Product 상품_생성(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
