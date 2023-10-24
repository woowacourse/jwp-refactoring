package kitchenpos.dto.request;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private final String name;
    private final long price;

    public ProductCreateRequest(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, new Price(price));
    }

    public String name() {
        return name;
    }

    public long price() {
        return price;
    }
}
