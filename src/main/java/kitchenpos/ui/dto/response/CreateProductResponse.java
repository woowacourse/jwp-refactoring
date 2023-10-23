package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class CreateProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static CreateProductResponse from(final Product product) {
        return new CreateProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    private CreateProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
