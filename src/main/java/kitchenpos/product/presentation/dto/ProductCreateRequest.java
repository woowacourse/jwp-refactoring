package kitchenpos.product.presentation.dto;

import kitchenpos.product.application.dto.ProductSaveRequest;

public class ProductCreateRequest {

    private String name;
    private Integer price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public ProductSaveRequest toRequest() {
        return new ProductSaveRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
