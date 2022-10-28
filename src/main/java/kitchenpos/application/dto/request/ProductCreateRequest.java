package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductCreateRequest {

    private String name;

    private BigDecimal price;

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
