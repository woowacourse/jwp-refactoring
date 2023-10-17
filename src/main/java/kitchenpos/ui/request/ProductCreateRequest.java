package kitchenpos.ui.request;

import kitchenpos.application.dto.ProductCreateDto;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductCreateDto toDto() {
        return new ProductCreateDto(name, price);
    }
}
