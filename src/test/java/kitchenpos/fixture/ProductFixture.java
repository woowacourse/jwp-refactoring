package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 스키야키() {
        final Product product = new Product();
        product.setName("스키야키");
        product.setPrice(BigDecimal.valueOf(11_900));
        return product;
    }

    public static Product 우동() {
        final Product product = new Product();
        product.setName("우동");
        product.setPrice(BigDecimal.valueOf(8_900));
        return product;
    }
}
