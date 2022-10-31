package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductDto {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductDto() {
    }

    public ProductDto(final Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public Product toEntity() {
        return new Product(id, name, price);
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
}
