package support.fixture;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductBuilder {

    private static int sequence = 1;

    private String name;
    private BigDecimal price;

    public ProductBuilder() {
        this.name = "상품" + sequence;
        this.price = BigDecimal.valueOf(sequence * 1000);

        sequence++;
    }

    public ProductBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setPrice(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new Product(name, price);
    }
}
