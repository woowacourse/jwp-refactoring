package kitchenpos.application.common.factory;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFactory {

    private ProductFactory() {
    }

    public static Product create(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
