package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public final class ProductFixture {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductFixture() {
    }

    public static ProductFixture builder() {
        return new ProductFixture();
    }

    public ProductFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductFixture withName(String name) {
        this.name = name;
        return this;
    }

    public ProductFixture withPrice(Long price) {
        this.price = BigDecimal.valueOf(price);
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
