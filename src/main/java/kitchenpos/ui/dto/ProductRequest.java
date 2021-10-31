package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank(message = "상품명이 null이거나 비어있습니다.")
    private String name;

    @NotNull(message = "상품의 가격이 null입니다.")
    @DecimalMin(value = "0", message = "상품의 가격이 음수입니다.")
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
