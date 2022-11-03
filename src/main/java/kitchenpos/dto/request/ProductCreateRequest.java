package kitchenpos.dto.request;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }
}
