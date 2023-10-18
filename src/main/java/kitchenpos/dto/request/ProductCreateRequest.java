package kitchenpos.dto.request;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private final String name;
    private final long price;

    public ProductCreateRequest(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public String name() {
        return name;
    }

    public long price() {
        return price;
    }
}
