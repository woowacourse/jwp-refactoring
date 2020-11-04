package kitchenpos.application.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {
    public static Product createProductRequest(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
