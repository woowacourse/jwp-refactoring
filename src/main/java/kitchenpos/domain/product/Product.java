package kitchenpos.domain.product;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private ProductName name;
    private ProductPrice price;

    public Product(ProductName name, ProductPrice price) {
        this(null, name, price);
    }

    public Product(Long id, ProductName name, ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
