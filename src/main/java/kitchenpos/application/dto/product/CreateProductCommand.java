package kitchenpos.application.dto.product;

import java.math.BigDecimal;

public class CreateProductCommand {

    private final String name;
    private final BigDecimal price;

    public CreateProductCommand(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }
}
