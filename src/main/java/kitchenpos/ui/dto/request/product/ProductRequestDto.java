package kitchenpos.ui.dto.request.product;

import java.math.BigDecimal;

public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    private ProductRequestDto() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
