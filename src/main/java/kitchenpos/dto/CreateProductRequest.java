package kitchenpos.dto;

import kitchenpos.domain.menu.Product;

public class CreateProductRequest {
    private final String name;
    private final Long price;

    public CreateProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }
}
