package kitchenpos.generator;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductGenerator {

    public static Product newInstance(String name, int price) {
        return newInstance(null, name, price);
    }

    public static Product newInstance(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}
