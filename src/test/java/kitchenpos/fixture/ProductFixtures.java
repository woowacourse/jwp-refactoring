package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixtures {

    public static Product createProduct(final String name, final BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product 후라이드() {
        return createProduct("후라이드", BigDecimal.valueOf(16000));
    }

    public static Product 양념치킨() {
        return createProduct("양념치킨", BigDecimal.valueOf(16000));
    }

    public static Product 반반치킨() {
        return createProduct("반반치킨", BigDecimal.valueOf(16000));
    }

    public static Product 통구이() {
        return createProduct("통구이", BigDecimal.valueOf(16000));
    }

    public static Product 간장치킨() {
        return createProduct("간장치킨", BigDecimal.valueOf(17000));
    }

    public static Product 순살치킨() {
        return createProduct("순살치킨", BigDecimal.valueOf(17000));
    }
}
