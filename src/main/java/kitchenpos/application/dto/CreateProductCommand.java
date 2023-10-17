package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class CreateProductCommand {

    private String name;
    private BigDecimal price;

    public Product toDomain() {
        return new Product(name, price);
    }

    public CreateProductCommand() {
    }

    public CreateProductCommand(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
