package kitchenpos.domain;

import kitchenpos.dto.request.ProductRequest;

public class Product {

    private Long id;
    private String name;
    private Price price;

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    public static Product from(ProductRequest productRequest) {
        return new Product(productRequest.getName(), new Price(productRequest.getPrice()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
