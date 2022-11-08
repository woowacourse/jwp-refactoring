package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductCreateRequest {

    private final String name;

    private final BigDecimal price;

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        validatePrice(price);
        this.price = price;
    }

    public void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
