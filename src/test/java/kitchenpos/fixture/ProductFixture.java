package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 후라이드() {
        final Product product = new Product();
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(16000));
        return product;
    }

    public static Product 양념치킨() {
        final Product product = new Product();
        product.setName("양념치킨");
        product.setPrice(BigDecimal.valueOf(16000));
        return product;
    }

    public static Product 반반치킨() {
        final Product product = new Product();
        product.setName("반반치킨");
        product.setPrice(BigDecimal.valueOf(16000));
        return product;
    }

    public static Product 통구이() {
        final Product product = new Product();
        product.setName("통구이");
        product.setPrice(BigDecimal.valueOf(16000));
        return product;
    }

    public static Product 간장치킨() {
        final Product product = new Product();
        product.setName("간장치킨");
        product.setPrice(BigDecimal.valueOf(17000));
        return product;
    }

    public static Product 순살치킨() {
        final Product product = new Product();
        product.setName("순살치킨");
        product.setPrice(BigDecimal.valueOf(17000));
        return product;
    }
}
