package kitchenpos.product.service.dto;

import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private Price price;

    public ProductRequest(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public ProductRequest() {
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
