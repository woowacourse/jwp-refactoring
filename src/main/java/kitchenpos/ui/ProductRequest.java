package kitchenpos.ui;

import java.math.BigDecimal;
import kitchenpos.application.dto.ProductCommand;

public class ProductRequest {

    private final String name;
    private final BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductCommand newProductCommand() {
        return new ProductCommand(name, price);
    }
}
