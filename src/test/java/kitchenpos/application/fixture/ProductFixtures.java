package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static final Product generateProduct(final String name, final BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static final Product 후라이드() {
        return generateProduct("후라이드", BigDecimal.valueOf(16000));
    }

    public static final Product 양념치킨() {
        return generateProduct("양념치킨", BigDecimal.valueOf(16000));
    }

    public static final Product 반반치킨() {
        return generateProduct("반반치킨", BigDecimal.valueOf(16000));
    }

    public static final Product 통구이() {
        return generateProduct("통구이", BigDecimal.valueOf(16000));
    }

    public static final Product 간장치킨() {
        return generateProduct("간장치킨", BigDecimal.valueOf(17000));
    }

    public static final Product 순살치킨() {
        return generateProduct("순살치킨", BigDecimal.valueOf(17000));
    }
}
