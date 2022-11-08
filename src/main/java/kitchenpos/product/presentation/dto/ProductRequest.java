package kitchenpos.product.presentation.dto;

import java.math.BigDecimal;
import kitchenpos.product.application.dto.ProductRequestDto;

public class ProductRequest {
    
    private String name;
    private BigDecimal price;

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductRequestDto toServiceDto() {
        return new ProductRequestDto(name, price);
    }
}
