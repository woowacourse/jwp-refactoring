package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 상품_생성_10000원() {
        return new Product("상품", new BigDecimal(10000));
    }

    public static Product 상품_생성(final String name, final BigDecimal price) {
        return new Product(name, price);
    }
}
