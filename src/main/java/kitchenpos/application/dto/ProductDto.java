package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductDto {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductDto() {
    }

    private ProductDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto from(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice());
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
