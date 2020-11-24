package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest() {
    }

    private ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductCreateRequest of(Product product) {
        Price price = product.getPrice();
        return new ProductCreateRequest(product.getName(), price.getPrice());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity(ProductCreateRequest productCreateRequest) {
        return new Product(productCreateRequest.name, Price.of(price));
    }
}
