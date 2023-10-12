package kitchenpos.supports;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    private Long id = null;
    private String name = "치킨";
    private BigDecimal price = new BigDecimal(20_000);

    private ProductFixture() {
    }

    public static ProductFixture fixture() {
        return new ProductFixture();
    }

    public ProductFixture id(Long id) {
        this.id = id;
        return this;
    }

    public ProductFixture name(String name) {
        this.name = name;
        return this;
    }

    public ProductFixture price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductFixture price(int price) {
        this.price = new BigDecimal(price);
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setId(id);

        product.setPrice(price);
        return product;
    }
}
