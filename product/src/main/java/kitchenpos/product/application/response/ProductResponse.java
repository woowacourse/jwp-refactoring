package kitchenpos.product.application.response;

import kitchenpos.product.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private String price;

    private ProductResponse() {
    }

    public ProductResponse(Long id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().toString());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
