package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    private ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductCreateRequest of(Product product) {
        return new ProductCreateRequest(product.getName(), product.getPrice());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity(ProductCreateRequest productCreateRequest) {
        return new Product(productCreateRequest.name, productCreateRequest.price);
    }
}
