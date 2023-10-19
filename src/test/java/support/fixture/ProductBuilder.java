package support.fixture;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductBuilder {

    private static int sequence = 1;

    private final Product product;

    public ProductBuilder() {
        product = new Product();
        product.setName("상품" + sequence);
        product.setPrice(BigDecimal.valueOf(1000L * sequence));

        sequence++;
    }

    public ProductBuilder setName(final String name) {
        product.setName(name);
        return this;
    }

    public ProductBuilder setPrice(final BigDecimal price) {
        product.setPrice(price);
        return this;
    }

    public Product build() {
        return product;
    }
}
