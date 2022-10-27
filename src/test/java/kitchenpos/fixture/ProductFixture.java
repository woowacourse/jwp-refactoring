package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        return product;
    }

    public static Product createProduct(String name) {
        Product product = new Product();
        product.setName(name);
        return product;
    }

    public static Product createProduct(String name, Long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
