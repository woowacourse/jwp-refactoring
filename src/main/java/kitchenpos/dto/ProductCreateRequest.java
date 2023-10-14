package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private Long price;

    public ProductCreateRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductCreateRequest{" +
               "name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}
