package kitchenpos.application.menu.dto.request.product;

import kitchenpos.domain.menu.Price;

import java.math.BigDecimal;

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
