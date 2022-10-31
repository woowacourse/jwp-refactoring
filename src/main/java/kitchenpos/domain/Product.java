package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final String name;
    private final Long price;

    public Product(Long id, String name, Long price) {
        validateName(name);
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(Long price) {
        if (price == null || price < 0L) {
            throw new IllegalArgumentException();
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
    }

    public Product(String name, Long price) {
        this(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
