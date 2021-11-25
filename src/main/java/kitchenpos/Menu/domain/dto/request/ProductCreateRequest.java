package kitchenpos.menu.domain.dto.request;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    protected ProductCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, new Price(price));
    }
}
