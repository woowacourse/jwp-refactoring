package kitchenpos.application.command;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import kitchenpos.domain.model.product.Product;

public class CreateProductCommand {
    @NotBlank
    private String name;
    @Min(0)
    private BigDecimal price;

    private CreateProductCommand() {
    }

    public CreateProductCommand(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(null, name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
