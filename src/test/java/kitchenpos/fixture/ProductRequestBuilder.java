package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.application.product.ProductRequest;

public class ProductRequestBuilder {

    private String name = "강정치킨";
    private BigDecimal price = BigDecimal.valueOf(14_000L);

    public static ProductRequestBuilder aProductRequest() {
        return new ProductRequestBuilder();
    }

    public ProductRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductRequestBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductRequest build() {
        return new ProductRequest(name, price);
    }
}
