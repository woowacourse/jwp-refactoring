package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequest {
    @NotNull
    private String name;
    @NotNull
    private BigDecimal price;

    private ProductRequest() {
    }

    private ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest from(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public Product toProduct() {
        return new Product(null, name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
