package kitchenpos.application.response;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductResponse from(Product product) {
        final ProductResponse productResponse = new ProductResponse();
        productResponse.id = product.getId();
        productResponse.name = product.getName();
        productResponse.price = product.getPrice();
        return productResponse;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
