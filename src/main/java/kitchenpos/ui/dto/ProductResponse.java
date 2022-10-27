package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private Long price;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().longValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
