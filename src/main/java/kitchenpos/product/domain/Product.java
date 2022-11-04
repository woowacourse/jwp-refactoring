package kitchenpos.product.domain;

import java.math.BigDecimal;
import kitchenpos.price.Price;

public class Product {
    private Long id;
    private String name;
    private Price price;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = new Price(price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }
    public BigDecimal calculatePriceByQuantity(long quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
