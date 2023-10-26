package kitchenpos.menu.vo;

import java.math.BigDecimal;

public class ProductSpecification {

    private final String name;
    private final Price price;

    public ProductSpecification(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public static ProductSpecification from(String name, BigDecimal price) {
        return new ProductSpecification(name, Price.valueOf(price));
    }

    public Price calculateTotalPrice(Quantity quantity) {
        return price.multiply(quantity.getValue());
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
