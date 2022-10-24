package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 짜장면 = createProduct("짜장면", 8_000);
    public static Product 짬뽕 = createProduct("짬뽕", 8_000);
    public static Product 탕수육 = createProduct("탕수육", 10_000);

    public static Product createProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}
