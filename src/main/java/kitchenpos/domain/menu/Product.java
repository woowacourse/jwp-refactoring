package kitchenpos.domain.menu;

import java.math.BigDecimal;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public BigDecimal calculateTotalPriceFromQuantity(int quantity) {
        return price.multiply(quantity).getValue();
    }
}
