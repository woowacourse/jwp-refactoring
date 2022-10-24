package kitchenpos.Fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
