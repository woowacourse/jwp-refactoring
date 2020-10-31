package kitchenpos.ui.dto.product;

import kitchenpos.domain.Product;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank
    private String name;

    @DecimalMin("0")
    private BigDecimal price;

    protected ProductRequest() {}

    public ProductRequest(final String name, final BigDecimal price) {
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
