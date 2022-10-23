package kitchenpos.domain;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Product {

    private final Long id;
    private final String name;
    private final Price price;

    public Product(Long id, String name, BigDecimal price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public BigDecimal calculateTotalPriceFromQuantity(int quantity) {
        return price.multiply(quantity).getValue();
    }
}
