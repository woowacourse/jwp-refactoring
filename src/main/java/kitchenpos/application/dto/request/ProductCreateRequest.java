package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct(final Price price) {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
