package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public enum ProductFixture {

    PRODUCT_PRICE_10000("제품1", 10000),
    PRODUCT_PRICE_1000("제품1", 1000),
    ;

    private final String name;
    private final int price;

    ProductFixture(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public Product 생성() {
        return new Product(this.name, new BigDecimal(this.price));
    }
}
