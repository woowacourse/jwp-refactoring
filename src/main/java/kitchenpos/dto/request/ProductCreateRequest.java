package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidProductPriceException;

public class ProductCreateRequest {
    @NotBlank
    private final String name;

    @NotNull
    @PositiveOrZero
    private final BigDecimal price;

    @JsonCreator
    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return Product.of(this.name, this.price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
