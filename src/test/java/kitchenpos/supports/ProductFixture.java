package kitchenpos.supports;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductFixture {

    private Long id = null;
    private String name = "치킨";
    private Price price = new Price(new BigDecimal(20_000));

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

    public ProductFixture price(Price price) {
        this.price = price;
        return this;
    }

    public ProductFixture price(int price) {
        this.price = new Price(new BigDecimal(price));
        return this;
    }

    public Product build() {
        return new Product(id, name, price);
    }
}
