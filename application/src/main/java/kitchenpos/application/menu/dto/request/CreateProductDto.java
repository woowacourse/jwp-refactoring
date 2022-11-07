package kitchenpos.application.menu.dto.request;

import java.math.BigDecimal;
import kitchenpos.common.domain.menu.Product;

public class CreateProductDto {

    private final String name;
    private final BigDecimal price;

    public CreateProductDto(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }
}
