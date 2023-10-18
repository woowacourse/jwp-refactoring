package kitchenpos.refactoring.application.dto;

import kitchenpos.refactoring.domain.Price;

public class ProductRequest {

    private String name;
    private Price price;

    public ProductRequest() {
    }

    public ProductRequest(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
