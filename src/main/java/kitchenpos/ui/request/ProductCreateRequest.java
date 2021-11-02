package kitchenpos.ui.request;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public static ProductCreateRequest create(String name, BigDecimal price) {
        final ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.name = name;
        productCreateRequest.price = price;
        return productCreateRequest;
    }

    public Product toEntity() {
        return Product.create(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
