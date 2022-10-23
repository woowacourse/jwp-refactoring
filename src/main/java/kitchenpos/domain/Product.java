package kitchenpos.domain;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Product {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Product(Long id, String name, BigDecimal price) {
        if (name == null || name.isBlank() || price == null || price.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }
}
