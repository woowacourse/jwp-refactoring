package kitchenpos.product.domain;

import kitchenpos.common.domain.Price;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Product {
    @Id
    private Long id;
    private String name;
    @Embedded.Empty
    private Price price;

    private Product() {
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
