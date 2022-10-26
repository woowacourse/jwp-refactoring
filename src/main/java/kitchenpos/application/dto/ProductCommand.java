package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCommand {

    private final String name;
    private final BigDecimal price;

    public ProductCommand(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product newProduct() {
        return new Product(name, price);
    }
}
