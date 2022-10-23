package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public enum ProductFixture {

    PRODUCT_짜장면(createProduct("짜장면", 8_000)),
    PRODUCT_짬뽕(createProduct("짬뽕", 8_000)),
    PRODUCT_탕수육(createProduct("탕수육", 10_000));

    private final Product value;

    ProductFixture(final Product value) {
        this.value = value;
    }

    public Product create() {
        return value;
    }

    public static Product createProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}
