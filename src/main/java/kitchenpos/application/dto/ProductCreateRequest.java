package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private int price;

    protected ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
