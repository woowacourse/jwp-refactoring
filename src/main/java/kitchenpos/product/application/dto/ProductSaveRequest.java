package kitchenpos.product.application.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductSaveRequest {

    private final String name;
    private final Integer price;

    public ProductSaveRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(Name.of(name), Price.valueOf(price));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
