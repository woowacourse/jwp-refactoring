package kitchenpos.product.service.dto;

import kitchenpos.common.vo.Price;

public class ProductRequest {

    private String name;
    private Price price;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final Price price) {
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
