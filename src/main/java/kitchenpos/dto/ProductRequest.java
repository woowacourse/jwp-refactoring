package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    public Product toEntity(Long id) {
        Product product = new Product(name, price);
        product.setId(id);
        return product;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
