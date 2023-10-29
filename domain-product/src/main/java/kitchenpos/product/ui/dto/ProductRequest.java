package kitchenpos.product.ui.dto;

import kitchenpos.product.domain.Product;
import kitchenpos.common.vo.Price;

import java.math.BigDecimal;

public class ProductRequest {

    private final String name;
    private final BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, new Price(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
