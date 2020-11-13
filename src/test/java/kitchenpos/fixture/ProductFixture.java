package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static Product createProduct(Long id, String name, Long price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price != null ? BigDecimal.valueOf(price) : null);

        return product;
    }

    public static Product createProductRequest(String name, Long price) {
        return createProduct(null, name, price);
    }
}
