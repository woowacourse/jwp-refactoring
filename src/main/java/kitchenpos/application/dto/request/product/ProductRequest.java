package kitchenpos.application.dto.request.product;

import kitchenpos.domain.product.Product;

public class ProductRequest {

    private String name;
    private int price;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product.ProductBuilder()
                .setPrice(price)
                .setName(name)
                .build();
    }
}
