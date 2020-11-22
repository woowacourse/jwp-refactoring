package kitchenpos.ui.dto.product;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Product;

public class ProductRequest {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0")
    private BigDecimal price;

    protected ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
