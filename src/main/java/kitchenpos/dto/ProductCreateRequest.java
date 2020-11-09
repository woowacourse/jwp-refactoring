package kitchenpos.dto;

import kitchenpos.domain.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductCreateRequest {
    @NotBlank(message = "상품의 이름은 반드시 존재해야 합니다!")
    private String name;

    @NotNull(message = "상품의 가격은 반드시 존재해야 합니다!")
    private BigDecimal price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(this.name, this.price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
