package kitchenpos.application.dto.response.product;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponseDto() {
    }

    public ProductResponseDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice());
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
