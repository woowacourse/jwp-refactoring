package fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductBuilder {

    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductBuilder init() {
        final ProductBuilder builder = new ProductBuilder();
        builder.id = null;
        builder.name = "상품";
        builder.price = BigDecimal.valueOf(10000);
        return builder;
    }

    public ProductBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder price(Long price) {
        this.price = BigDecimal.valueOf(price);
        return this;
    }

    public Product build() {
        return new Product(
                this.id,
                this.name,
                this.price
        );
    }
}
