package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.generic.Price;
import kitchenpos.domain.product.Product;

public class ProductBuilder {

    private String name = "강정치킨";
    private BigDecimal price = BigDecimal.valueOf(14_000L);

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new Product(name, new Price(price));
    }
}
