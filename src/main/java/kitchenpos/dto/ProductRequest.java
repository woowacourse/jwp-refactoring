package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductRequest {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductRequest(Long id, String name, BigDecimal price) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(id, name, price);
    }
}
