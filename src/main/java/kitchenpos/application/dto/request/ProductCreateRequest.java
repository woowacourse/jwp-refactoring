package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private final String name;
    private final BigDecimal price;

    private ProductCreateRequest() {
        this(null, null);
    }

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
