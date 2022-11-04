package kitchenpos.menu.application.dto.request.product;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Price;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return new Price(price);
    }
}
