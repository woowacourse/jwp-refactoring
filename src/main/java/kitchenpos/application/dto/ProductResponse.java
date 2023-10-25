package kitchenpos.application.dto;

import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private long price;

    public ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().intValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
