package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import kitchenpos.application.dto.CreateProductDto;

public class CreateProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public CreateProductResponse(final CreateProductDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.price = dto.getPrice();
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
