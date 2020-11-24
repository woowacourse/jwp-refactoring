package kitchenpos.product.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import kitchenpos.product.domain.Product;

public class ProductCreateRequest {

    @NotEmpty
    private String name;

    @Min(0)
    private Long price;

    public ProductCreateRequest(@NotEmpty String name, @Min(0) Long price) {
        this.name = name;
        this.price = price;
    }

    public ProductCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
