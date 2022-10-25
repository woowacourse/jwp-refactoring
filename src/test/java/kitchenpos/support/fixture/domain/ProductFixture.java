package kitchenpos.support.fixture.domain;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public enum ProductFixture {

    APPLE_1000("apple", new BigDecimal(1000)),
    PEAR_2000("pear", new BigDecimal(2000)),
    BANANA_3000("banana", new BigDecimal(3000))
    ;

    private final String name;
    private final BigDecimal price;

    ProductFixture(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product getProduct() {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public Product getProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
