package kitchenpos.ui.dto;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductResponse {

    private String name;
    private BigDecimal price;

    public ProductResponse(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getName(), product.getPrice());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
