package kitchenpos.product.application.dto;

import kitchenpos.product.domain.Product;

public class ProductCreateRequest {

    private String name;
    private Long price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final Long price) {
        this.name = name;
        this.price = price;
    }

    public Product toDomain() {
        return new Product(null, name, price);
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
