package kitchenpos.product.dto;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

import kitchenpos.product.domain.Product;

public class ProductRequest {

    @NotEmpty(message = "이름을 입력해주세요.")
    private final String name;
    @DecimalMin(value = "0", message = "가격은 0 이상이여야 합니다.")
    private final BigDecimal price;

    @ConstructorProperties({"name", "price"})
    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
