package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.Money;
import kitchenpos.domain.Product;

public class CreateProductCommand {

    private String name;
    private BigDecimal price;

    public Product toDomain() {
        return new Product(name, new Money(price));
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
