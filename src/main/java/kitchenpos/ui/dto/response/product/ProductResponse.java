package kitchenpos.ui.dto.response.product;

import java.math.BigDecimal;
import kitchenpos.application.dto.response.product.ProductResponseDto;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponse() {
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(ProductResponseDto productResponseDto) {
        return new ProductResponse(productResponseDto.getId(), productResponseDto.getName(), productResponseDto.getPrice());
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
