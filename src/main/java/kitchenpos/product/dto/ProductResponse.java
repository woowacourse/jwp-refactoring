package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class ProductResponse {

    private final String name;
    private final BigDecimal price;

    private ProductResponse(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from (Product product){
        return new ProductResponse(product.getName(), product.getPrice());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
