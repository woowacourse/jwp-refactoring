package kitchenpos.ui.request;

import kitchenpos.domain.Product;

public class ProductRequest {

    private String name;
    private Long price;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }
}
